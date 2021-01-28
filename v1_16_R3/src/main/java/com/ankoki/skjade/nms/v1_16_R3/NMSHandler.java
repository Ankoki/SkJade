package com.ankoki.skjade.nms.v1_16_R3;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NMSHandler implements NMS {
    private final Set<EnumPlayerTeleportFlags> SET = new HashSet<>(Arrays.asList(EnumPlayerTeleportFlags.X, EnumPlayerTeleportFlags.Y, EnumPlayerTeleportFlags.Z, EnumPlayerTeleportFlags.X_ROT, EnumPlayerTeleportFlags.Y_ROT));

    @Override
    public void sendPacketPlayOutPosition(Player player, float h, float v) {
        if (player == null) return;
        Location loc = player.getLocation();
        PacketPlayOutPosition packet = new PacketPlayOutPosition(loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw() + h,
                loc.getPitch() + v,
                SET,
                0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}