package com.ankoki.skjade.hooks.holograms;

import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.bukkitevents.HologramClickEvent;
import com.ankoki.skjade.hooks.holograms.bukkitevents.HologramTouchEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HologramManager {
    private static final Map<String, Hologram> ALL_HOLOGRAMS = new ConcurrentHashMap<>();
    private static final Map<Hologram, List<HologramLine>> ALL_LINES = new ConcurrentHashMap<>();
    private static final Map<Hologram, Location> ALL_LOCATIONS = new ConcurrentHashMap<>();

    public static void createHologram(String key, Location location, boolean visibility, boolean allowPlaceholders) {
        Hologram hologram = HologramsAPI.createHologram(SkJade.getInstance(), location);
        hologram.getVisibilityManager().setVisibleByDefault(visibility);
        hologram.setAllowPlaceholders(allowPlaceholders);
        ALL_HOLOGRAMS.put(key, hologram);
        ALL_LOCATIONS.put(hologram, location);
    }

    public static void deleteHologram(Hologram... holograms) {
        for (Hologram hologram : holograms) {
            ALL_HOLOGRAMS.entrySet().removeIf(entry -> entry.getValue().equals(hologram));
            if (!hologram.isDeleted()) hologram.delete();
        }
    }

    public static void addTextLine(Hologram hologram, String line) {
        List<HologramLine> lines = ALL_LINES.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(hologram.appendTextLine(line));
        ALL_LINES.remove(hologram);
        ALL_LINES.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : ALL_HOLOGRAMS.entrySet()) {
            if (entry.getValue() == hologram) {
                ALL_HOLOGRAMS.remove(entry.getKey());
                ALL_HOLOGRAMS.put(entry.getKey(), hologram);
                return;
            }
        }
    }

    public static void addItemLine(Hologram hologram, ItemStack item) {
        List<HologramLine> lines = ALL_LINES.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(hologram.appendItemLine(item));
        ALL_LINES.remove(hologram);
        ALL_LINES.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : ALL_HOLOGRAMS.entrySet()) {
            if (entry.getValue() == hologram) {
                ALL_HOLOGRAMS.remove(entry.getKey());
                ALL_HOLOGRAMS.put(entry.getKey(), hologram);
                return;
            }
        }
    }

    public static void setLine(Hologram hologram, int index, String text) {
        if (!(hologram.getLine(index) instanceof TextLine)) return;
        TextLine line = (TextLine) hologram.getLine(index);
        line.setText(text);
        for (Map.Entry<String, Hologram> entry : ALL_HOLOGRAMS.entrySet()) {
            if (entry.getValue() == hologram) {
                ALL_HOLOGRAMS.remove(entry.getKey());
                ALL_HOLOGRAMS.put(entry.getKey(), hologram);
                return;
            }
        }
    }

    public static void removeLine(Hologram hologram, int line) {
        line--;
        if (line < 0) line = 0;
        List<HologramLine> lines = ALL_LINES.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.remove(line);
        hologram.removeLine(line);
        ALL_LINES.remove(hologram);
        ALL_LINES.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : ALL_HOLOGRAMS.entrySet()) {
            if (entry.getValue() == hologram) {
                ALL_HOLOGRAMS.remove(entry.getKey());
                ALL_HOLOGRAMS.put(entry.getKey(), hologram);
                return;
            }
        }
    }

    public static void removeLine(HologramLine line) {
        Hologram hologram = line.getParent();
        if (hologram == null) return;
        List<HologramLine> lines = ALL_LINES.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.remove(line);
        line.removeLine();
        ALL_LINES.put(hologram, lines);
        for (Map.Entry<String, Hologram> entry : ALL_HOLOGRAMS.entrySet()) {
            if (entry.getValue() == hologram) {
                ALL_HOLOGRAMS.remove(entry.getKey());
                ALL_HOLOGRAMS.put(entry.getKey(), hologram);
                return;
            }
        }
    }

    public static HologramLine getLine(Hologram hologram, int line) {
        line--;
        if (line < 0) line = 0;
        List<HologramLine> lines = ALL_LINES.get(hologram);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines.get(line);
    }

    public static HologramLine[] getLines(Hologram hologram) {
        return (HologramLine[]) ALL_LINES.get(hologram).toArray();
    }

    public static Hologram getHologram(String key) {
        return ALL_HOLOGRAMS.get(key);
    }

    public static Location getHoloLocation(Hologram holo) {
        return ALL_LOCATIONS.get(holo);
    }

    public static String getIDFromHolo(Hologram hologram) {
        for (Map.Entry<String, Hologram> entry : ALL_HOLOGRAMS.entrySet()) {
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

    //looking into
    public static void imageHologram() {
    }

    public enum TouchType {
        INTERACTABLE,
        CLICKABLE,
        TOUCHABLE;
    }
}