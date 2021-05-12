package com.ankoki.skjade.nms.v1_13_R2;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class NMSHandler implements NMS {

    @Override
    public void sendDemo(Player player) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(5, 0);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void playFakeDamage(Player[] of, Player[] to) {
        for (Player p : of) {
            EntityPlayer handle = ((CraftPlayer) p).getHandle();
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation(handle, 1);
            for (Player p1 : to) {
                ((CraftPlayer) p1).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public void changeSkyColour(int colour, Player[] who) {
    }

    @Override
    public void setRaining(Player[] players, boolean rain) {
    }

    @Override
    public void showMiningStage(int stage, Location[] locations, Player[] players, int entityId, boolean remove) {
        stage = Math.min(stage, 9);
        stage = Math.max(stage, 0);
        if (remove) stage = 100;
        for (Location loc : locations) {
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(entityId, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), stage);
            Arrays.stream(players).forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
        }
    }

    @Override
    public void hideEntity(Player[] players, Entity[] entities) {
        for (Entity entity : entities) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
            Arrays.stream(players).forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
        }
    }
}