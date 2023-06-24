package com.ankoki.skjade.hooks.holograms.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SKJHoloBuilder {

    private final String key;
    private final Location location;
    private final Map<Integer, List<SKJHoloLine>> pages = new ConcurrentHashMap<>();
    private final List<Player> hide = new ArrayList<>();
    private final Map<Integer, List<Player>> show = new ConcurrentHashMap<>();
    private final List<HologramTrigger> triggers = new ArrayList<>();

    private boolean still, persistent;

    /**
     * Creates a new builder with the core values.
     *
     * @param key the key.
     * @param location the location.
     */
    public SKJHoloBuilder(String key, Location location) {
        this.key = key;
        this.location = location;
    }

    /**
     * Adds a page to the current builder.
     *
     * @param page the page to set. If the provider doesn't support pages, use page 0.
     * @param lines the lines to add.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder addPage(int page, Object[] lines) {
        List<SKJHoloLine> current = this.pages.getOrDefault(page, new ArrayList<>());
        for (Object unparsed : lines) current.add(HoloHandler.get().getCurrentProvider().parseLine(unparsed));
        this.pages.put(page, current);
        return this;
    }

    /**
     * Sets whether the hologram should be static if the provider supports it.
     *
     * @param still true if it should be static.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder setStatic(boolean still) {
        this.still = still;
        return this;
    }

    /**
     * Sets whether the hologram should be saved over restart if the provider supports it.
     *
     * @param persistent true if it should be persistent.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder setPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    /**
     * Adds players to show the hologram to if the provider supports it.
     *
     * @param players the players to show the hologram to.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder showTo(int page, Player... players) {
        show.put(page, Arrays.asList(players));
        return this;
    }

    /**
     * Adds players to hide the hologram from if the provider supports it.
     *
     * @param players the players to hide the hologram from.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder hideFrom(Player... players) {
        hide.addAll(Arrays.asList(players));
        return this;
    }

    /**
     * Adds an interaction for the hologram.
     *
     * @param trigger the trigger to execute.
     * @return the current builder for chaining.
     */
    public SKJHoloBuilder addInteraction(HologramTrigger trigger) {
        this.triggers.add(trigger);
        return this;
    }

    /**
     * Builds the current builder into an SKJHolo object.
     *
     * @return the built hologram.
     */
    public SKJHolo build() {
        SKJHolo hologram = HoloHandler.get().getCurrentProvider().createHolo(key, location, pages);
        if (hologram == null) return null;
        hologram.setStatic(still);
        hologram.setPersistent(persistent);
        for (Map.Entry<Integer, List<Player>> entry : show.entrySet())
            hologram.showTo(entry.getKey(), entry.getValue().toArray(new Player[0]));
        for (HologramTrigger trigger : triggers)
            HoloHandler.get().registerInteraction(hologram, trigger);
        hologram.hideFrom(hide.toArray(hide.toArray(new Player[0])));
        return hologram;
    }

}
