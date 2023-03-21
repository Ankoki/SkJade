package com.ankoki.skjade.elements.lasers;

import ch.njol.skript.util.Timespan;

import java.util.HashMap;
import java.util.Map;

public class LaserManager {

    private static LaserManager instance = new LaserManager();

    public static LaserManager get() {
        return instance;
    }

    private final Map<String, Laser> ALL_LASERS = new HashMap<>();

    public void createLaser(String id, Laser laser) {
        ALL_LASERS.put(id, laser);
    }

    public void deleteLaser(String key) {
        ALL_LASERS.remove(key);
    }

    public Laser getLaser(String id) {
        return ALL_LASERS.get(id);
    }

    public boolean keyInUse(String key) {
        return ALL_LASERS.containsKey(key);
    }

    public int secondsFromTimespan(Timespan timespan) {
        return timespan == null ? -1 : (int) Math.ceil(timespan.getTicks_i() / 20D);
    }

}