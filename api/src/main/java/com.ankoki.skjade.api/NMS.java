package com.ankoki.skjade.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface NMS {
    boolean canBreak(ItemStack item1, Material mat);
}