package com.ankoki.skjade.elements.lasers;

import java.util.HashMap;
import java.util.Map;

public class LaserManager {
    private static final Map<String, Laser> ALL_LASERS = new HashMap<>();

    public static void createLaser(String id, Laser laser) {
        ALL_LASERS.put(id, laser);
    }

    public static Laser getLaser(String id) {
        return ALL_LASERS.get(id);
    }
}