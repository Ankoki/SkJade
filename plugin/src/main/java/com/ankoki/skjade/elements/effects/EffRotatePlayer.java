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
import com.ankoki.skjade.SkJade;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Rotate Player")
@Description("Rotates a player without teleportation.")
@Examples("rotate all players by 3 horizontally and 7 vertically")
@Since("1.0.0")
public class EffRotatePlayer extends Effect {

    static {
        if (SkJade.isNmsEnabled()) {
            Skript.registerEffect(EffRotatePlayer.class,
                    "rotate %players% by %number% [horizontally] [[and] %-number% [vertically]]:");
        }
    }

    private Expression<Player> players;
    private Expression<Number> horizontal;
    private Expression<Number> vertical;

    @Override
    protected void execute(Event event) {
        float h = horizontal.getSingle(event).floatValue();
        float v = vertical == null ? 0 : vertical.getSingle(event).floatValue();
        Arrays.stream(players.getArray(event)).forEach(player -> {
            SkJade.getNmsHandler().sendPacketPlayOutPosition(player, h, v);
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "rotate " + players.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        horizontal = (Expression<Number>) exprs[1];
        vertical = (Expression<Number>) exprs[2];
        return true;
    }
}
