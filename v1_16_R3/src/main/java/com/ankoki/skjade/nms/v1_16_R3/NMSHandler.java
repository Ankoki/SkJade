package com.ankoki.skjade.nms.v1_16_R3;

import com.ankoki.skjade.api.NMS;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.PacketPlayOutGameStateChange;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSHandler implements NMS {

    @Override
    public void sendDemo(Player player) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.f, 0);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public boolean canBreak(ItemStack item, Material material) {
        net.minecraft.server.v1_16_R3.ItemStack craftItem = CraftItemStack.asNMSCopy(item);
        if (material.isBlock()) {
            IBlockData data = fromMat(material);
            if (data.isRequiresSpecialTool()) {
                return craftItem.canDestroySpecialBlock(data);
            } else {
                return true;
            }
        }
        return false;
    }

    private IBlockData fromMat(Material mat) {
        Block nmsBlock = CraftMagicNumbers.getBlock(mat);
        return nmsBlock.getBlockData();
    }
}