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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("World Border Warning Time")
@Description("Allows you to get and set the warning time of a world border.")
@Examples("set the warning time of player's world's world border to 20 seconds")
@Since("1.3.0")
public class ExprWorldBorderWarningTime extends SimpleExpression<Timespan> {

    static {
        Skript.registerExpression(ExprWorldBorderWarningTime.class, Timespan.class, ExpressionType.SIMPLE,
                "[skjade] [world[ ]]border warning time of %world%",
                "[skjade] %world%'s [world[ ]]border warning time",
                "[skjade] [the] warning time of %world%'s [world[ ]]border");
    }

    private Expression<World> worldExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Timespan[] get(Event event) {
        World world = worldExpr.getSingle(event);
        if (world == null) return new Timespan[0];
        return new Timespan[]{Timespan.fromTicks_i(world.getWorldBorder().getWarningTime() * 20L)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return worldExpr.toString(e, debug) + "'s world border warning time";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode != ChangeMode.REMOVE_ALL) {
            return CollectionUtils.array(Timespan.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        World world = worldExpr.getSingle(e);
        if (world == null) return;
        if (mode == ChangeMode.ADD || mode == ChangeMode.SET || mode == ChangeMode.REMOVE) {
            if (delta.length < 1 || !(delta[0] instanceof Timespan timespan)) return;
            int i = (int) timespan.getTicks_i() / 20;
            int currentTime = world.getWorldBorder().getWarningTime();
            if (mode == ChangeMode.ADD) {
                world.getWorldBorder().setWarningTime(Math.max(0, currentTime + i));
            } else if (mode == ChangeMode.REMOVE) {
                world.getWorldBorder().setWarningTime(Math.max(0, currentTime - i));
            } else {
                world.getWorldBorder().setWarningTime(Math.max(0, i));
            }
            return;
        }
        world.getWorldBorder().setWarningTime(0);
    }

}
