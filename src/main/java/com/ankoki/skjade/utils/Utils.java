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

    /**
     * Colours a given text using the default minecraft codes.
     *
     * @param string the string to colour.
     * @return the coloured string.
     */
    public static String coloured(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Changes a number to the roman numeral equivalent.
     *
     * @param number the number to change.
     * @return the roman numeral.
     */
    public static String toRoman(int number) {
        if (number == 0)
            return "0";
        int floor = ROMAN_NUMERALS.floorKey(number);
        if (number == floor) return ROMAN_NUMERALS.get(number);
        return ROMAN_NUMERALS.get(floor) + toRoman(number - floor);
    }

    /**
     * Changes a string to be coloured as a rainbow.
     *
     * @param message the message to colour.
     * @param pastel true if the rainbow should be pastel.
     * @return the coloured string.
     */
    public static String rainbow(String message, boolean pastel) {
        double freq1 = 0.3;
        double freq2 = 0.3;
        double freq3 = 0.3;
        double amp1 = 0;
        double amp2 = 2;
        double amp3 = 4;
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

    /**
     * Makes the given text monochrome.
     *
     * @param string the string to uncolour.
     * @return the uncoloured string.
     */
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

    /**
     * Gets the minor version of the current server version.
     *
     * @return the minor version.
     */
    public static int getMinecraftMinor() {
        try {
            String packageName = SkJade.getInstance().getServer().getClass().getPackage().getName();
            String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            return Integer.parseInt(version.split("_")[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets the patch version of the current server version.
     *
     * @return the patch version.
     */
    public static int getMinecraftPatch() {
        try {
            String version = Bukkit.getMinecraftVersion().split("-R")[0];
            return Integer.parseInt(version.split("\\.")[2]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns true if a plugin under the given name exists.
     *
     * @param name the name of the plugin.
     * @return true if enabled.
     */
    public static boolean isPluginEnabled(String name) {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(name);
    }

}
