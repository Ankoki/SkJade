package com.ankoki.skjade.hooks.holograms.api;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface HoloProvider {

    /**
     * Parses an object into a SKJHoloLine.
     * @param line the unparsed line.
     * @return the parsed line.
     */
    SKJHoloLine parseLine(Object line);

    /**
     * Gets the id of the provider.
     * @return the id of the provider.
     */
    @NotNull String getId();

    /**
     * Checks if the provider supports hologram pages.
     * @return if the provider supports pages.
     */
    boolean supportsPages();

    /**
     * Checks if the provider supports persistent holograms.
     * @return if the provider supports persistence.
     */
    boolean supportsPersistence();

    /**
     * Checks if the provider supports holograms not always facing the player.
     * @return if the provider supports static.
     */
    boolean supportsStatic();

    /**
     * Checks if the provider supports holograms being shown to individual players.
     * @return if provider supports per player holograms.
     */
    boolean supportsPerPlayer();

    /**
     * Creates a new hologram using the provider.
     * @param name the name/key to give the hologram.
     * @param location the location to spawn it at.
     * @param pages the pages of lines to give the hologram. If the provider doesn't support pages, only use page index 0.
     * @return the created hologram.
     */
    @NotNull SKJHolo createHolo(String name, Location location, Map<Integer, List<SKJHoloLine>> pages);
}
