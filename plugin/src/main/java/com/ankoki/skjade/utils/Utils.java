package com.ankoki.skjade.utils;

import org.bukkit.ChatColor;

public class Utils {

    public static String coloured(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public enum SpellType {
        GENERIC,
        ENTITY,
        GENERIC_PROLONGED,
        ENTITY_PROLONGED;
    }
}
