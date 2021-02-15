package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;
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
    private static final TreeMap<Integer, String> rn = new TreeMap<>();

    static {
        rn.put(1000, "M");
        rn.put(900, "CM");
        rn.put(500, "D");
        rn.put(400, "CD");
        rn.put(100, "C");
        rn.put(90, "XC");
        rn.put(50, "L");
        rn.put(40, "XL");
        rn.put(10, "X");
        rn.put(9, "IX");
        rn.put(5, "V");
        rn.put(4, "IV");
        rn.put(1, "I");
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
        int l = rn.floorKey(number);
        if (number == l) {
            return rn.get(number);
        }
        return rn.get(l) + toRoman(number - l);
    }

    public static boolean versionIsMinimum(Version version) {
        Version serverVersion = SkJade.getServerVersion();
        if (serverVersion == null || version == null) return true;
        int svMaj = Integer.parseInt(serverVersion.name().split("_")[1]);
        int svMin = Integer.parseInt(serverVersion.name().split("_")[2]);
        int vMaj = Integer.parseInt(version.name().split("_")[1]);
        int vMin = Integer.parseInt(version.name().split("_")[2]);
        int sv = (svMaj * 10) + svMin;
        int v = (vMaj * 10) + vMin;
        return sv >= v;
    }

    //testing use 0.3, 0.3, 0.3, 0, 2, 4
    public static String rainbow(String message, double freq1, double freq2, double freq3,
                                 double phase1, double phase2, double phase3, boolean pastel) {
        int center = pastel ? 200 : 128;
        int width = pastel ? 55 : 127;
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String s : message.split("")) {
            float red = (float) (Math.sin(freq1 * i + phase1) * width + center);
            float green = (float) (Math.sin(freq2 * i + phase2) * width + center);
            float blue = (float) (Math.sin(freq3 * i + phase3) * width + center);
            if (red > 255 || red < 0) red = 0;
            if (green > 255 || green < 0) green = 0;
            if (blue > 255 || blue < 0) blue = 0;
            builder.append(net.md_5.bungee.api.ChatColor.of(new Color((int) red, (int) green, (int) blue))).append(s);
            i++;
        }
        return builder.toString();
    }

    public static List<Location> getCircle(Location centre, double radius, double totalBlocks) {
        World world = centre.getWorld();
        double increment = _2PI/totalBlocks;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < totalBlocks; i++) {
            double angle = i * increment;
            double x = centre.getX() + (radius * Math.cos(angle));
            double z = centre.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, centre.getY(), z));
        }
        return locations;
    }

    public static List<Location> getStarPoints(Location center, double radius, int vertices) {
        List<Location> locations = new ArrayList<>();
        double delta = _2PI / vertices;
        boolean bug = false;
        for (double theta = 0; theta < _2PI; theta += delta) {
            if (!bug && vertices == 6) {
                bug = true;
                continue;
            }
            Vector offset = new Vector(Math.sin(theta) * radius, 0, Math.cos(theta) * radius);
            //gigi said suck it twink
            Location vertex = center.clone();
            vertex.add(offset);
            if (!locations.contains(vertex)) {
                locations.add(vertex);
            }
        }
        return locations;
    }

    public static List<Location> getLine(Location loc1, Location loc2, double space) {
        List<Location> points = new ArrayList<>();
        double distance = loc1.distance(loc2);
        Vector p1 = loc1.toVector();
        Vector p2 = loc2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        for (double length = 0; length < distance; p1.add(vector)) {
            points.add(p1.toLocation(loc1.getWorld()));
            length += space;
        }
        return points;
    }

    public enum SpellType {
        GENERIC,
        ENTITY,
        GENERIC_PROLONGED,
        ENTITY_PROLONGED;
    }
}
