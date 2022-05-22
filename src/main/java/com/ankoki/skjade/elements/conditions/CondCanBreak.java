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

    private Expression<ItemType> itemExpr;
    private Expression<Block> blockExpr;
    private boolean negate;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        itemExpr = (Expression<ItemType>) exprs[0];
        blockExpr = (Expression<Block>) exprs[1];
        negate = parseResult.mark == 1;
        return true;
    }

    @Override
    public boolean check(Event event) {
        ItemType item = itemExpr.getSingle(event);
        Block block = blockExpr.getSingle(event);
        if (item == null || block == null) return false;
        return negate == block.getDrops(item.getRandom()).isEmpty();
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return itemExpr.toString(event, b) + " can break " + blockExpr.toString(event, b);
    }
}
