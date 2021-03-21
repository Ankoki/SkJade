package com.ankoki.skjade.hooks.holograms;

import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.bukkitevents.HologramClickEvent;
import com.ankoki.skjade.hooks.holograms.bukkitevents.HologramTouchEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.CollectableLine;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramManager {
    private static final Map<String, Hologram> allHolograms = new HashMap<>();
    private static final Map<Hologram, List<HologramLine>> allLines = new HashMap<>();
    private static final Map<Hologram, Location> allLocations = new HashMap<>();

    public static void createHologram(String key, Location location, boolean visibility, boolean allowPlaceholders) {
        Hologram hologram = HologramsAPI.createHologram(SkJade.getInstance(), location);
        hologram.getVisibilityManager().setVisibleByDefault(visibility);
        hologram.setAllowPlaceholders(allowPlaceholders);
        allHolograms.put(key, hologram);
        allLocations.put(hologram, location);
    }

    public static void deleteHologram(Hologram... holograms) {
        for (Hologram hologram : holograms) {
            allHolograms.entrySet().removeIf(entry -> entry.getValue().equals(hologram));
            if (!hologram.isDeleted()) hologram.delete();
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
                return;
            }
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
                return;
            }
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
                return;
            }
        }
    }

    public static void removeLine(HologramLine line) {
        Hologram hologram = line.getParent();
        if (hologram == null) return;
        List<HologramLine> lines = allLines.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.remove(line);
        line.removeLine();
        allLines.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : allHolograms.entrySet()) {
            if (entry.getValue() == hologram) {
                allHolograms.remove(entry.getKey());
                allHolograms.put(entry.getKey(), hologram);
                return;
            }
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

    public static Location getHoloLocation(Hologram holo) {
        return allLocations.get(holo);
    }

    public static String getIDFromHolo(Hologram hologram) {
        for (Map.Entry<String, Hologram> entry : allHolograms.entrySet()) {
            if (entry.getValue() == hologram) {
                return entry.getKey();
            }
        }
        return "";
    }

    public static void handleTouch(Hologram hologram, HologramLine line) {
        TouchableLine touchable = (TouchableLine) line;
        if (touchable.getTouchHandler() == null) {
            touchable.setTouchHandler(player -> {
                HologramClickEvent event = new HologramClickEvent(player, hologram, touchable);
                Bukkit.getPluginManager().callEvent(event);
            });
        }
    }

    public static void handlePickup(Hologram hologram, HologramLine line) {
        if (!(line instanceof ItemLine)) return;
        CollectableLine collectable = (CollectableLine) line;
        if (collectable.getPickupHandler() == null) {
            collectable.setPickupHandler(player -> {
                HologramTouchEvent event = new HologramTouchEvent(player, hologram, collectable);
                Bukkit.getPluginManager().callEvent(event);
            });
        }
    }

    public static void removeTouch(HologramLine line) {
        ((TouchableLine) line).setTouchHandler(null);
    }

    public static void removePickup(HologramLine line) {
        ((CollectableLine) line).setPickupHandler(null);
    }

    public enum TouchType {
        INTERACTABLE,
        CLICKABLE,
        TOUCHABLE;
    }
}