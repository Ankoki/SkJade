package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Time;
import ch.njol.skript.util.Timeperiod;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Time at Player")
@Description("Returns the player's time.")
@Examples("broadcast \"%the time at player%\"")
@Since("1.0.0")
public class ExprTimeAtPlayer extends SimpleExpression<Time> {

    static {
        Skript.registerExpression(ExprTimeAtPlayer.class, Time.class, ExpressionType.COMBINED,
                "[the] time at [(player|the player)] %player%");
    }

    private Expression<Player> player;

    @Nullable
    @Override
    protected Time[] get(Event e) {
        if (player == null) return new Time[0];
        Player p = player.getSingle(e);
        if (p == null) return new Time[0];
        return new Time[]{new Time((int) p.getPlayerTime())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Time> getReturnType() {
        return Time.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "the time at " + player.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(Time.class, Timeperiod.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (player == null) return;
        Player p = player.getSingle(e);
        if (p != null) {
            final int time = delta[0] instanceof Time ? ((Time) delta[0]).getTicks() : ((Timeperiod) delta[0]).start;
            p.setPlayerTime(time, false);
        }
    }
}