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
import com.ankoki.skjade.utils.Laser;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Stop Laser")
@Description("Stops any lasers in progress.")
@Examples("stop the laser with id \"my twins big like tia tamara ugh wha\"")
@Since("insert version")
public class EffStopLaser extends Effect {

    static {
        Skript.registerEffect(EffStopLaser.class,
                "stop %lasers%");
    }

    private Expression<Laser> laser;

    @Override
    protected void execute(Event e) {
        if (laser == null) return;
        Laser[] lasers = laser.getArray(e);
        for (Laser l : lasers) {
            if (l.isStarted()) l.stop();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "stop " + laser.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laser = (Expression<Laser>) exprs[0];
        return true;
    }
}
