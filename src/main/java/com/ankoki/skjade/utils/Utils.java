package com.ankoki.skjade.utils;

import com.ankoki.skjade.utils.events.HologramClickEvent;
import com.ankoki.skjade.utils.events.HologramTouchEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.CollectableLine;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utils {

    public static String coloured(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
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

    public enum SpellType {
        GENERIC,
        ENTITY,
        GENERIC_PROLONGED,
        ENTITY_PROLONGED;
    }
}
