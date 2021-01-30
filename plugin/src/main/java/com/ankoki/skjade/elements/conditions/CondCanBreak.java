package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Can Break Block")
@Description("Checks if an item type can break a block.")
@Examples("if {_item} can break {_block}:")
@RequiredPlugins("Minecraft 1.15+")
@Since("1.0.0")
public class CondCanBreak extends Condition {

    static {
        Skript.registerCondition(CondCanBreak.class,
                "%itemtype% can break %itemtype%");
    }

    private Expression<ItemType> item1;
    private Expression<ItemType> item2;

    @Override
    public boolean check(Event event) {
        ItemType i1 = item1.getSingle(event);
        ItemType i2 = item2.getSingle(event);
        if (i1 == null || i2 == null) return false;
        ItemStack item = i1.getRandom();
        ItemStack matItem = i2.getRandom();
        if (item == null || matItem == null) return false;
        Material material = matItem.getType();
        return SkJade.getNMS().canBreak(item, material);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "can break";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        item1 = (Expression<ItemType>) exprs[0];
        item2 = (Expression<ItemType>) exprs[1];
        return SkJade.isNmsEnabled();
    }
}
