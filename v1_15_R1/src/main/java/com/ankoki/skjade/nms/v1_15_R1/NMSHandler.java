package com.ankoki.skjade.nms.v1_15_R1;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_15_R1.PacketPlayOutPosition;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class NMSHandler implements NMS {

    @Override
    public void sendPacketPlayOutPosition(Player player, float h, float v) {
        Location loc = player.getLocation();
        PacketPlayOutPosition packet = new PacketPlayOutPosition(loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw() + h,
                loc.getPitch() + v,
                new HashSet<>(),
                0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
