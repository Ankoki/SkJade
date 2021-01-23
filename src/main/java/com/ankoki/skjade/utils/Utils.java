package com.ankoki.skjade.utils;

import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    public static String coloured(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendActionbar(Player player, String message) {

    }

    public static Spell getSpellFromName(String spellName) {
        for (Spell spell : ElementalsAPI.getAllSpells()) {
            if (spell.getSpellName().equalsIgnoreCase(spellName)) {
                return spell;
            }
        }
        return null;
    }
}
