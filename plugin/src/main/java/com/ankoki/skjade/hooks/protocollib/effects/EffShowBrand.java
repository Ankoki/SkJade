package com.ankoki.skjade.hooks.protocollib.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

@Name("Server Brand")
@Description("Sends a server brand to players.")
@Examples("send brand \"washing machine heart by mitski\" to all players")
@Since("1.3.0")
public class EffShowBrand extends Effect {
    private static final MinecraftKey BRAND_KEY = new MinecraftKey("brand");
    private static final PacketContainer PAYLOAD_PACKET = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);

    static {
        Skript.registerEffect(EffShowBrand.class,
                "(send|show) [server] brand %string% (to|for) %players%");
    }

    private Expression<String> brand;
    private Expression<Player> players;

    @Override
    protected void execute(Event e) {
        if (brand == null || players == null) return;
        String b = brand.getSingle(e);
        Player[] p = players.getArray(e);
        if (b == null || p.length == 0) return;

        PAYLOAD_PACKET.getMinecraftKeys().write(0, BRAND_KEY);
        byte[] bytes = b.getBytes(StandardCharsets.UTF_8);
        byte[] byt = new byte[256];
        byt[0] = (byte) b.length();
        System.arraycopy(bytes, 0, byt, 1, 255);
        ByteBuf buffer = Unpooled.copiedBuffer(byt);
        Object seraliser = MinecraftReflection.getPacketDataSerializer(buffer);
        PAYLOAD_PACKET.getModifier().withType(ByteBuf.class).write(0, seraliser);
        for (Player player : p) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, PAYLOAD_PACKET);
            } catch (InvocationTargetException ignored) {}
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "send brand " + brand.toString(e, debug) + " to " + players.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        brand = (Expression<String>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }
}
