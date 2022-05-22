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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Force Sleep")
@Description("Forces a player to sleep at a location as long as there is a bed at that location.")
@Examples("force event-player to sleep at {%player's uuid%::permabed}")
@Since("1.0.0")
public class EffForceSleep extends Effect {

    static {
        Skript.registerEffect(EffForceSleep.class,
                "(force|make) %players% [to] sleep at %location%");
    }

    private Expression<Player> playerExpr;
    private Expression<Location> locationExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        locationExpr = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Location location = locationExpr.getSingle(event);
        if (location == null || playerExpr == null) return;
        Arrays.stream(playerExpr.getArray(event)).forEach(p -> {
            if (p == null) return;
            p.sleep(location, true);
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "force " + playerExpr.toString(event, b) + " to sleep at " + locationExpr.toString(event, b);
    }
}
