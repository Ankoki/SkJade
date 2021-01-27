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

    private Expression<Player> player;
    private Expression<Location> location;

    @Override
    protected void execute(Event event) {
        Location loc = location.getSingle(event);
        if (loc == null) return;
        Arrays.stream(player.getArray(event)).forEach(p -> {
            if (p == null) return;
            p.sleep(loc, true);
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "force " + player.toString(event, b) + " to sleep";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }
}
