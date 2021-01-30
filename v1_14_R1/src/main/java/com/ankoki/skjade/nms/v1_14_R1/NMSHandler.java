package com.ankoki.skjade.nms.v1_14_R1;

import com.ankoki.skjade.api.NMS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NMSHandler implements NMS {
    @Override
    public boolean canBreak(ItemStack item, Material material) {
        return false;
    }
}