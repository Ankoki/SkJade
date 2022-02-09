package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("World Border Size Over Time")
@Description("Sets the worldborder of a world over time.")
@Examples("set player's world's world border to 10 over 10 seconds")
@Since("1.3.0")
public class EffWorldBorderSizeOverTime extends Effect {

    static {
        Skript.registerEffect(EffWorldBorderSizeOverTime.class,
                "set [skjade] [world[ ]]border size of %world% to %number% over %timespan%",
                "set [skjade] %world%'s [world[ ]]border size to %number% over %timespan%");
    }

    private Expression<World> worldExpr;
    private Expression<Number> sizeExpr;
    private Expression<Timespan> timeExpr;

    @Override
    protected void execute(Event e) {
        World world = worldExpr.getSingle(e);
        Number number = sizeExpr.getSingle(e);
        Timespan time = timeExpr.getSingle(e);
        if (world == null || number == null || time == null) return;
        world.getWorldBorder().setSize(number.doubleValue(), time.getTicks_i() / 20L);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set world border size of " + worldExpr.toString(e, debug) + " to " + sizeExpr.toString(e, debug) + " over " + timeExpr.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        sizeExpr = (Expression<Number>) exprs[1];
        timeExpr = (Expression<Timespan>) exprs[2];
        return true;
    }
}
