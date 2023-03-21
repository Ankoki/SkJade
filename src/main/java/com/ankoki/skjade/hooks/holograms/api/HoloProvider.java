package com.ankoki.skjade.hooks.holograms.api;

import ch.njol.skript.lang.Trigger;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.structure.Structure;

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
     * Parses a list of objects into a list of SKJHoloLines.
     * @param lines the unparsed lines.
     * @return the parsed lines.
     */
    List<SKJHoloLine> parseLines(List<Object> lines);

    /**
     * Parses a foreign click type into an SkJade ClickType.
     * @param click the foreign click-type, or a String representation of such.
     * @return the translated.
     */
    ClickType parseClickType(Object click);

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
     * Checks if the provider supports click events.
     * @param singleLine will be true if we want to check if it supports single line clicks.
     * @return true if supported, else false.
     */
    boolean supportsOnClick(boolean singleLine);

    /**
     * Checks if the provider supports touch events.
     * @return true if supported, else false.
     */
    boolean supportsOnTouch();

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
     * Checks if the provider supports entity lines.
     * @return if provider supports entity lines.
     */
    boolean supportsEntityLines();

    /**
     * Checks if the provider supports registering custom placeholders.
     * @return if provider supports custom placeholders.
     */
    boolean supportsCustomPlaceholders();

    /**
     * Registers a placeholder if the provider supports it.
     * @param name the name of the placeholder.
     * @param refreshRate the refresh rate of the placeholder in seconds.
     * @param trigger the trigger to execute. Must be used before trying to get the value from the return.
     * @param structure the structure which is used.
     */
    void registerPlaceholder(String name, int refreshRate, Trigger trigger, Structure structure);

    /**
     * Creates a new hologram using the provider. Will be null if creation failed.
     * @param name the name/key to give the hologram.
     * @param location the location to spawn it at.
     * @param pages the pages of lines to give the hologram. If the provider doesn't support pages, only use page index 0.
     * @return the created hologram.
     */
    @Nullable SKJHolo createHolo(String name, Location location, Map<Integer, List<SKJHoloLine>> pages);

    /**
     * Gets an SKJHolo from the providers class.
     * @return the hologram class.
     */
    SKJHolo getHologramFrom(Object object);

    /**
     * Call this if you need to set up anything on initiation.
     */
    default void setup() {}
}
