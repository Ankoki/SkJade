package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Reset World Border")
@Description("Resets the world border of a world.")
@Examples("Reset the world border of player's world")
@Since("1.3.0")
public class EffWorldBorderReset extends Effect {

    static {
        Skript.registerEffect(EffWorldBorderReset.class,
                "[skjade] reset ([the] [world[ ]]border [of] %worlds%|%worlds%'s world border)");
    }

    private Expression<World> worldExpr;

    @Override
    protected void execute(Event e) {
        if (worldExpr == null) return;
        World[] worlds = worldExpr.getArray(e);
        for (World world : worlds) {
            world.getWorldBorder().reset();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "reset the world border of " + worldExpr.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        return true;
    }
}
