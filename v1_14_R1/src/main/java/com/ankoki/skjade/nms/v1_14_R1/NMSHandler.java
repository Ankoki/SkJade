package com.ankoki.skjade.nms.v1_14_R1;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_14_R1.PacketPlayOutPosition;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandler implements NMS {

    @Override
    public void sendPacketPlayOutPosition(Player player, float h, float v) {
        Location loc = player.getLocation();
        PacketPlayOutPosition packet = new PacketPlayOutPosition(loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw() + h,
                loc.getPitch() + v,
                null,
                0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}