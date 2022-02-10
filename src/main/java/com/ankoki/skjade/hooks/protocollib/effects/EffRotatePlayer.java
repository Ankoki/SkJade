package com.ankoki.skjade.hooks.protocollib.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
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
        Skript.registerEffect(EffRotatePlayer.class,
                "rotate %players% by %number% [horizontally] [[and] %-number% [vertically]]");
    }

    private static final PacketContainer PACKET =
            ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.POSITION);

    private Expression<Player> players;
    private Expression<Number> horizontal;
    private Expression<Number> vertical;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        horizontal = (Expression<Number>) exprs[1];
        if (exprs.length > 1) vertical = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (players == null) return;
        Number num1 = horizontal.getSingle(e);
        if (num1 == null) return;
        float h = num1.floatValue();
        float v = 0;
        if (vertical != null) {
            Number num2 = vertical.getSingle(e);
            if (num2 == null) return;
            v = num2.floatValue();
        }
        float finalV = v;
        Arrays.stream(players.getArray(e)).forEach(player -> {
            if (player == null) return;
            Location loc = player.getLocation();
            PACKET.getDoubles().write(0, loc.getX());
            PACKET.getDoubles().write(1, loc.getY());
            PACKET.getDoubles().write(2, loc.getZ());
            PACKET.getFloat().write(0, loc.getYaw() + h);
            PACKET.getFloat().write(1, loc.getPitch() + finalV);
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, PACKET);
            } catch (InvocationTargetException ignored) {
            }
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "rotate " + players.toString(event, b) + " by " + horizontal.toString(event, b) +
                (vertical == null ? " and " + vertical.toString(event, b) + " vertically" : "");
    }
}
