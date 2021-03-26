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

@Name("Make Laser Change Colour")
@Description("Makes a laser change colour. You cannot specify this coloUr.")
@Examples("force the laser with id \"coochie vegan she a veggie\" to change colour")
@Since("insert version")
public class EffChangeLaserColour extends Effect {

    static {
        Skript.registerEffect(EffChangeLaserColour.class,
                "(force|make) %lasers% [to] change colo[u]r[[']s]");
    }

    private Expression<Laser> laser;

    @Override
    protected void execute(Event e) {
        Laser[] lasers = laser.getArray(e);
        for (Laser laser : lasers) {
            if (laser.isStarted()) {
                try {
                    laser.callColorChange();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "force " + laser.toString(e, debug) + " to change colours";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laser = (Expression<Laser>) exprs[0];
        return true;
    }
}
