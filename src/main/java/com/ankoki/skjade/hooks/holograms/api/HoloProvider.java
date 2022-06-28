package com.ankoki.skjade.hooks.holograms.api;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface HoloProvider {

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
     * Creates a new hologram using the provider.
     * @param name the name/key to give the hologram.
     * @param location the location to spawn it at.
     * @param lines the lines to give the hologram.
     * @return the created hologram.
     */
    @NotNull SKJHolo createHolo(String name, Location location, List<SKJHoloLine> lines);
}
