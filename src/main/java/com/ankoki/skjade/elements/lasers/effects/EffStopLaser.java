package com.ankoki.skjade.elements.lasers.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Stop Laser")
@Description("Stops any lasers in progress.")
@Examples("stop the laser with id \"my twins big like tia tamara ugh wha\"")
@Since("1.3.1")
public class EffStopLaser extends Effect {

    static {
        if (Laser.isEnabled())
            Skript.registerEffect(EffStopLaser.class,
                "stop %lasers%");
    }

    private Expression<Laser> laserExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laserExpr = (Expression<Laser>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Laser[] lasers = laserExpr.getArray(event);
        for (Laser laser : lasers) {
            if (laser.isStarted()) laser.stop(true);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "stop " + laserExpr.toString(e, debug);
    }
}
