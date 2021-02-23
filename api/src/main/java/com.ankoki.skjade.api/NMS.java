package com.ankoki.skjade.api;

import org.bukkit.entity.Player;

public interface NMS {
    void sendDemo(Player player);
    void playFakeDamage(Player[] of, Player[] to);
    void changeSkyColour(int colour, Player[] who);
    void rain(Player[] players, boolean rain);
}