package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.TreeMap;

public final class Utils {
    private Utils(){}
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

    public static int getMinecraftMinor() {
        int ver = 0;
        try {
            String packageName = SkJade.getInstance().getServer().getClass().getPackage().getName();
            String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            ver = Integer.parseInt(version.split("_")[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ver;
    }

    public static boolean isPluginEnabled(String name) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}
