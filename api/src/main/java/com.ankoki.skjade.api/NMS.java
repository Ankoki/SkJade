package com.ankoki.skjade.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

//TODO Rewrite lasers into packets instead of reflection (this probably isnt necessary)
public interface NMS {
    void sendDemo(Player player);
    void playFakeDamage(Player[] of, Player[] to);
    void changeSkyColour(int colour, Player[] who);
    void setRaining(Player[] players, boolean rain);
    void showMiningStage(int stage, Location[] locations, Player[] players, int entityId, boolean remove);
}