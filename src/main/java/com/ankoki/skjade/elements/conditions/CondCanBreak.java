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
                "%itemtype% can(1¦(not|[']t)|) break %block%");
    }

    private Expression<ItemType> item1;
    private Expression<Block> block;
    private boolean negate;

    @Override
    public boolean check(Event event) {
        ItemType i1 = item1.getSingle(event);
        Block b = block.getSingle(event);
        if (i1 == null || b == null) return false;
        return negate == b.getDrops(i1.getRandom()).isEmpty();
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return item1.toString(event, b) + " can break " + block.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        item1 = (Expression<ItemType>) exprs[0];
        block = (Expression<Block>) exprs[1];
        negate = parseResult.mark == 1;
        return true;
    }
}
