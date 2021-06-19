package com.ankoki.skjade.nms.v1_17_R1;

import com.ankoki.skjade.api.NMS;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class NMSHandler implements NMS {

    @Override
    public void sendDemo(Player player) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.f, 0);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().b.sendPacket(packet);
    }

    @Override
    public void playFakeDamage(Player[] of, Player[] to) {
        for (Player p : of) {
            EntityPlayer handle = ((CraftPlayer) p).getHandle();
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation(handle, 1);
            for (Player p1 : to) {
                ((CraftPlayer) p1).getHandle().b.sendPacket(packet);
            }
        }
    }

    @Override
    public void changeSkyColour(int colour, Player[] who) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.h, colour);
        Arrays.stream(who).forEach(p -> ((CraftPlayer) p).getHandle().b.sendPacket(packet));
    }

    @Override
    public void setRaining(Player[] players, boolean rain) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(rain ? PacketPlayOutGameStateChange.c : PacketPlayOutGameStateChange.b, 0);
        Arrays.stream(players).forEach(p -> ((CraftPlayer) p).getHandle().b.sendPacket(packet));
    }

    @Override
    public void showMiningStage(int stage, Location[] locations, Player[] players, int entityId, boolean remove) {
        stage = Math.min(stage, 9);
        stage = Math.max(stage, 0);
        if (remove) stage = 100;
        for (Location loc : locations) {
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(entityId, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), stage);
            Arrays.stream(players).forEach(p -> ((CraftPlayer) p).getHandle().b.sendPacket(packet));
        }
    }

    @Override
    public void hideEntity(Player[] players, Entity[] entities) {
        for (Entity entity : entities) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
            Arrays.stream(players).forEach(p -> ((CraftPlayer) p).getHandle().b.sendPacket(packet));
        }
    }
}