package com.ankoki.skjade.hooks.holograms;

import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramManager {
    private static final Map<String, Hologram> allHolograms = new HashMap<>();
    private static final Map<Hologram, List<HologramLine>> allLines = new HashMap<>();

    public static void createHologram(String key, Location location) {
        Hologram hologram = HologramsAPI.createHologram(SkJade.getInstance(), location);
        allHolograms.put(key, hologram);
    }

    public static void deleteHologram(Hologram... holograms) {
        for (Map.Entry<String, Hologram> entry : allHolograms.entrySet()) {
            for (Hologram hologram : holograms) {
                if (entry.getValue() == hologram) allHolograms.remove(entry.getKey());
                if (!hologram.isDeleted()) {
                    hologram.delete();
                }
            }
        }
    }

    public static void deleteHologram(String key) {
        Hologram hologram = allHolograms.get(key);
        allHolograms.remove(key);
        hologram.delete();
    }

    public static void addTextLine(Hologram hologram, String line) {
        List<HologramLine> lines = allLines.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(hologram.appendTextLine(line));
        allLines.remove(hologram);
        allLines.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : allHolograms.entrySet()) {
            if (entry.getValue() == hologram) {
                allHolograms.remove(entry.getKey());
                allHolograms.put(entry.getKey(), hologram);
            }
            return;
        }
    }

    public static void addItemLine(Hologram hologram, ItemStack item) {
        List<HologramLine> lines = allLines.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(hologram.appendItemLine(item));
        allLines.remove(hologram);
        allLines.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : allHolograms.entrySet()) {
            if (entry.getValue() == hologram) {
                allHolograms.remove(entry.getKey());
                allHolograms.put(entry.getKey(), hologram);
            }
            return;
        }
    }

    public static void removeLine(Hologram hologram, int line) {
        line--;
        if (line < 0) line = 0;
        List<HologramLine> lines = allLines.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.remove(line);
        hologram.removeLine(line);
        allLines.remove(hologram);
        allLines.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : allHolograms.entrySet()) {
            if (entry.getValue() == hologram) {
                allHolograms.remove(entry.getKey());
                allHolograms.put(entry.getKey(), hologram);
            }
            return;
        }
    }

    public static HologramLine getLine(Hologram hologram, int line) {
        line--;
        if (line < 0) line = 0;
        List<HologramLine> lines = allLines.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines.get(line);
    }

    public static HologramLine[] getLines(Hologram hologram) {
        return (HologramLine[]) allLines.get(hologram).toArray();
    }

    public static Hologram getHologram(String key) {
        return allHolograms.get(key);
    }
}