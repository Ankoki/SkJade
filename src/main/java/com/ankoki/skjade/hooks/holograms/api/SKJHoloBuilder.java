package com.ankoki.skjade.hooks.holograms.api;

import com.ankoki.skjade.hooks.holograms.HoloManager;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SKJHoloBuilder {

    private final String key;
    private final Location location;
    private final Map<Integer, List<SKJHoloLine>> pages = new ConcurrentHashMap<>();

    private boolean still, persistent;

    /**
     * Creates a new builder with the core values.
     * @param key the key.
     * @param location the location.
     */
    public SKJHoloBuilder(String key, Location location) {
        this.key = key;
        this.location = location;
    }

    /**
     * Adds a page to the current builder.
     * @param page the page to set. If the provider doesn't support pages, use page 0.
     * @param lines the lines to add.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder addPage(int page, Object[] lines) {
        List<SKJHoloLine> current = this.pages.getOrDefault(page, new ArrayList<>());
        for (Object unparsed : lines) current.add(HoloManager.get().getCurrentProvider().parseLine(unparsed));
        this.pages.put(page, current);
        return this;
    }

    /**
     * Sets whether the hologram should be static if the provider supports it.
     * @param still true if it should be static.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder setStatic(boolean still) {
        this.still = still;
        return this;
    }

    /**
     * Sets whether the hologram should be saved over restart if the provider supports it.
     * @param persistent true if it should be persistent.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder setPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    /**
     * Builds the current builder into an SKJHolo object.
     * @return the built hologram.
     */
    public SKJHolo build() {
        SKJHolo hologram = HoloManager.get().getCurrentProvider().createHolo(key, location, pages);
        hologram.setStatic(still);
        hologram.setPersistent(persistent);
        return hologram;
    }
}
