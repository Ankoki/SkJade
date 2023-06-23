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
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Make Laser Change Colour")
@Description("Makes a laser change colour. You cannot specify this coloUr.")
@Examples("force the laser with id \"coochie vegan she a veggie\" to change colour")
@Since("1.3.1")
public class EffChangeLaserColour extends Effect {

    static {
        if (SkJade.getInstance().isNmsEnabled())
            Skript.registerEffect(EffChangeLaserColour.class,
                "(force|make) %lasers% [to] change colo[u]r[[']s]");
    }

    private Expression<Laser> laserExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laserExpr = (Expression<Laser>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Laser[] lasers = laserExpr.getArray(e);
        for (Laser laser : lasers) {
            if (laser.isStarted()) {
                try {
                    ((Laser.GuardianLaser) laser).callColorChange();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "force " + laserExpr.toString(e, debug) + " to change colours";
    }
}
