package com.ankoki.skjade.hooks.holograms;

import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class HologramManager {
    private static final HashMap<String, Hologram> allHolograms = new HashMap<>();

    public static void createHologram(String key, Location location) {
        Hologram hologram = HologramsAPI.createHologram(SkJade.getInstance(), location);
        allHolograms.put(key, hologram);
    }

    public static void deleteHologram(String key) {
        Hologram hologram = allHolograms.get(key);
        allHolograms.remove(key);
        hologram.delete();
    }

    public static void addTextLine(String key, String line) {
        Hologram hologram = allHolograms.get(key);
        hologram.appendTextLine(line);
        allHolograms.remove(key);
        allHolograms.put(key, hologram);
    }

    public static void addItemLine(String key, ItemStack item) {
        Hologram hologram = allHolograms.get(key);
        hologram.appendItemLine(item);
        allHolograms.remove(key);
        allHolograms.put(key, hologram);
    }

    public static void removeLine(String key, int line) {
        Hologram hologram = allHolograms.get(key);
        hologram.removeLine(line);
        allHolograms.remove(key);
        allHolograms.put(key, hologram);
    }
}