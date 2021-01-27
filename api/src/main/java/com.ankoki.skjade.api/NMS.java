package com.ankoki.skjade.api;

import org.bukkit.entity.Player;

public interface NMS {
    void sendPacketPlayOutPosition(Player player, float h, float v);
}