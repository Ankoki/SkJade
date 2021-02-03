package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Can Break Block")
@Description("Checks if an item type can break a block.")
@Examples("if player's tool can break player's target block:")
@Since("1.0.0")
public class CondCanBreak extends Condition {

    static {
        Skript.registerCondition(CondCanBreak.class,
                "%itemtype% can break %block%");
    }

    private Expression<ItemType> item1;
    private Expression<Block> item2;

    @Override
    public boolean check(Event event) {
        ItemType i1 = item1.getSingle(event);
        Block i2 = item2.getSingle(event);
        if (i1 == null || i2 == null) return false;
        return !i2.getDrops(i1.getRandom()).isEmpty();
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "can break";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        item1 = (Expression<ItemType>) exprs[0];
        item2 = (Expression<Block>) exprs[1];
        return true;
    }
}
