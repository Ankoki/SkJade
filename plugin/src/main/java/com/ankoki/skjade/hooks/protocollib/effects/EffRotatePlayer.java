package com.ankoki.skjade.hooks.protocollib.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Name("Rotate Player")
@Description("Rotates a player without teleportation.")
@Examples("rotate all players by 3 horizontally and 7 vertically")
@RequiredPlugins("ProtocolLib")
@Since("1.0.0")
public class EffRotatePlayer extends Effect {

    static {
        if (SkJade.isNmsEnabled()) {
            Skript.registerEffect(EffRotatePlayer.class,
                    "rotate %players% by %number% [horizontally] [[and] %-number% [vertically]]");
        }
    }

    private Expression<Player> players;
    private Expression<Number> horizontal;
    private Expression<Number> vertical;

    @Override
    protected void execute(Event event) {
        if (players == null) return;
        float h = horizontal.getSingle(event).floatValue();
        float v = vertical == null ? 0 : vertical.getSingle(event).floatValue();
        Arrays.stream(players.getArray(event)).forEach(player -> {
            if (player == null) return;
            Location loc = player.getLocation();
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.POSITION);
            packet.getDoubles().write(0, loc.getX());
            packet.getDoubles().write(1, loc.getY());
            packet.getDoubles().write(2, loc.getZ());
            packet.getFloat().write(0, loc.getYaw() + h);
            packet.getFloat().write(1, loc.getPitch() + v);
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (InvocationTargetException ignored) {}
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
        if (exprs.length > 1) {
            vertical = (Expression<Number>) exprs[2];
        }
        return true;
    }
}
