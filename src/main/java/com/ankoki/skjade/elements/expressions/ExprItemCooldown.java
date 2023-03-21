package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Item Cooldown")
@Description("Gets and sets the cooldown for a material for a player.")
@Examples("set the cooldown of wool for player to 10 seconds")
@Since("1.3.0")
public class ExprItemCooldown extends SimpleExpression<Timespan> {

    static {
        Skript.registerExpression(ExprItemCooldown.class, Timespan.class, ExpressionType.SIMPLE,
                "[the] cooldown of %itemtypes% for %player%");
    }

    private Expression<Player> playerExpr;
    private Expression<ItemType> itemTypeExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        itemTypeExpr = (Expression<ItemType>) exprs[0];
        playerExpr = (Expression<Player>) exprs[1];
        return true;
    }

    @Nullable
    @Override
    protected Timespan[] get(Event event) {
        Player player = playerExpr.getSingle(event);
        ItemType[] items = itemTypeExpr.getArray(event);
        if (player == null || items.length == 0) return new Timespan[0];
        Timespan[] timespans = new Timespan[items.length];
        int i = 0;
        for (ItemType item : items) {
            timespans[i] = Timespan.fromTicks_i(player.getCooldown(item.getMaterial()));
            i++;
        }
        return timespans;
    }

    @Override
    public boolean isSingle() {
        return itemTypeExpr.isSingle();
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the cooldown of " + itemTypeExpr.toString(e, debug) + " for " + playerExpr.toString(e, debug);
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET || mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
            return CollectionUtils.array(Timespan.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        Player player = playerExpr.getSingle(e);
        ItemType[] items = itemTypeExpr.getArray(e);
        Timespan time = mode == ChangeMode.SET ? (Timespan) delta[0] : null;
        if (player == null) return;
        switch (mode) {
            case SET:
                if (time != null) {
                    for (ItemType item : items) {
                        player.setCooldown(item.getMaterial(), (int) time.getTicks_i());
                    }
                }
                break;
            case DELETE:
            case RESET:
                for (ItemType item : items) {
                    player.setCooldown(item.getMaterial(), 0);
                }
        }
    }

}
