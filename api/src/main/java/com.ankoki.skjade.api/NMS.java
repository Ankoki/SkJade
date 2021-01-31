package com.ankoki.skjade.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMS {
    public void sendDemo(Player player);
    boolean canBreak(ItemStack item1, Material material);
}