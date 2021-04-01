package com.ankoki.skjade.utils;

import ch.njol.util.VectorMath;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public final class Utils {
    private Utils(){}
    private static final double _2PI = 6.283185307179586;
    private static final double _3PI = Math.PI + Math.PI + Math.PI;
    private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();

    static {
        ROMAN_NUMERALS.put(1000, "M");
        ROMAN_NUMERALS.put(900, "CM");
        ROMAN_NUMERALS.put(500, "D");
        ROMAN_NUMERALS.put(400, "CD");
        ROMAN_NUMERALS.put(100, "C");
        ROMAN_NUMERALS.put(90, "XC");
        ROMAN_NUMERALS.put(50, "L");
        ROMAN_NUMERALS.put(40, "XL");
        ROMAN_NUMERALS.put(10, "X");
        ROMAN_NUMERALS.put(9, "IX");
        ROMAN_NUMERALS.put(5, "V");
        ROMAN_NUMERALS.put(4, "IV");
        ROMAN_NUMERALS.put(1, "I");
    }

    public static String coloured(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Checks the version of a plugin compared too a version you want
     * it to match up with, for example, if you wanted to make sure if a plugin
     * had a version higher than 1.2, you can use versionChecker(Plugin, 1, 2);
     *
     * @param plugin Plugin is the plugin you want to check the version of, usually
     *               obtained through Bukkit.getPluginManager.getPlugin("pluginName");
     * @param major  The major of the version you want to check against, if there was
     *               a version 1.2, 1 would be the major.
     * @param minor  The minor of the version you want to check against, if there was
     *               a version 1.2, 2 would be the minor.
     * @return Wether the plugins version is equal to or greater than the inputted
     * version.
     */
    public static boolean checkPluginVersion(Plugin plugin, int major, int minor) {
        major *= 10;
        int pluginVer = Integer.parseInt(plugin.getDescription().getVersion().replace(".", ""));
        int required = major + minor;
        return pluginVer >= required;
    }

    public static String toRoman(int number) {
        int l = ROMAN_NUMERALS.floorKey(number);
        if (number == l) {
            return ROMAN_NUMERALS.get(number);
        }
        return ROMAN_NUMERALS.get(l) + toRoman(number - l);
    }

    public static String rainbow(String message, double freq1, double freq2, double freq3,
                                 double amp1, double amp2, double amp3, boolean pastel) {
        int center = pastel ? 200 : 128;
        int width = pastel ? 55 : 127;
        StringBuilder builder = new StringBuilder();
        message = coloured(message);

        int i = 0;
        String currentColourCode = "";
        for (String s : message.split("")) {
            if (s.equals("§")) {
                i++;
                continue;
            }
            if (i > 0) {
                if (message.charAt(i - 1) == '§') {
                    if (s.equalsIgnoreCase("r")) {
                        currentColourCode = "";
                        i++;
                        continue;
                    } else if ("abcdefklmnor0123456789".contains(s)) {
                        currentColourCode += "§" + s;
                        i++;
                        continue;
                    }
                }
            }
            float red = (float) (Math.sin(freq1 * i + amp1) * width + center);
            float green = (float) (Math.sin(freq2 * i + amp2) * width + center);
            float blue = (float) (Math.sin(freq3 * i + amp3) * width + center);
            if (red > 255 || red < 0) red = 0;
            if (green > 255 || green < 0) green = 0;
            if (blue > 255 || blue < 0) blue = 0;
            builder.append(net.md_5.bungee.api.ChatColor.of(new Color((int) red, (int) green, (int) blue)));
            builder.append(currentColourCode).append(s);
            i++;
        }
        return builder.toString();
    }

    public static String simpleRainbow(String message, boolean pastel) {
        return rainbow(message, 0.3, 0.3, 0.3, 0, 2, 4, pastel);
    }

    public static String monochrome(String string) {
        double frequency = 0.3;
        int amplitude = 127;
        int center = 128;
        StringBuilder builder = new StringBuilder();
        int i = 0;
        String currentColourCode = "";
        for (String s : string.split("")) {
            if (i > 0) {
                if (string.charAt(i - 1) == '§') {
                    if (s.equalsIgnoreCase("r")) {
                        currentColourCode = "";
                        i++;
                        continue;
                    } else if ("abcdefklmnor0123456789".contains(s)) {
                        currentColourCode += "§" + s;
                        i++;
                        continue;
                    }
                }
            }
            double v = Math.sin(frequency * i) * amplitude + center;
            builder.append(net.md_5.bungee.api.ChatColor.of(new Color((int) v, (int) v, (int) v)));
            builder.append(currentColourCode).append(s);
            i++;
        }
        return builder.toString();
    }

    public enum SpellType {
        GENERIC("generic"),
        ENTITY("entity"),
        GENERIC_PROLONGED("prolonged generic"),
        ENTITY_PROLONGED("prolonged entity");

        private final String pretty;
        SpellType(String pretty) {
            this.pretty = pretty;
        }
        public String getPretty() {
            return pretty;
        }
    }
}
