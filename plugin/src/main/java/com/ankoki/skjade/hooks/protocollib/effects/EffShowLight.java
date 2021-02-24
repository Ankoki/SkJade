package com.ankoki.skjade.hooks.protocollib.effects;

import ch.njol.skript.Skript;
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
import org.jetbrains.annotations.Nullable;

public class EffShowLight extends Effect {

    /*static {
        Skript.registerEffect(EffShowLight.class,
                "show light [with [a]] [light] level [of] %number% at %locations% [to %-players%]");
    }*/

    private Expression<Number> number;
    private Expression<Location> location;
    private Expression<Player> players;

    @Override
    protected void execute(Event e) {
        if (number == null || location == null) return;
        int n = number.getSingle(e).intValue();
        Location[] loc = location.getArray(e);
        Player[] ps = players.getArray(e);
        for (Location l : loc) {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.LIGHT_UPDATE);
            packet.getIntegers().write(0, l.getChunk().getX());
            packet.getIntegers().write(1, l.getChunk().getZ());
            packet.getBooleans().write(0, true);
            packet.getIntegers().write(2, (int) Math.floor(l.getY()));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "show light with a light level of " + number.toString(e, debug) + " at " + location.toString(e, debug) +
                (players == null ? "" : " to " + players.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        number = (Expression<Number>) exprs[0];
        location = (Expression<Location>) exprs[1];
        players = (Expression<Player>) exprs[2];
        return true;
    }
}
