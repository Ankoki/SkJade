package com.ankoki.skjade.nms.v1_16_R3;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSHandler implements NMS {

    @Override
    public boolean canBreak(ItemStack item, Material material) {
        net.minecraft.server.v1_16_R3.ItemStack craftItem = CraftItemStack.asNMSCopy(item);
        IBlockData data = (IBlockData) material.createBlockData();
        return craftItem.canDestroySpecialBlock(data);
    }
}