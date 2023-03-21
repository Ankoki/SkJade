package com.ankoki.skjade.hooks.holograms.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface SKJHolo {

    /**
     * Registers the hologram under the given key.
     * @param key the key to register.
     * @return true if successful.
     */
    default boolean register(String key) {
         return HoloHandler.get().registerHolo(key, this);
    }

    /**
     * Gets the current holograms key, for storage purposes.
     * @return the holograms key.
     */
    @NotNull String getKey();

    /**
     * Moves the hologram to given location.
     * @param location the location to move the hologram to.
     */
    void teleport(Location location);

    /**
     * Appends a line to the hologram.
     * @param line the line to add.
     */
    void appendLine(int page, SKJHoloLine line);

    /**
     * Sets a line to the text.
     * @param index the index to set.
     * @param line the text to set it to.
     */
    void setLine(int page, int index, SKJHoloLine line);

    /**
     * Sets the lines of the hologram.
     * @param lines the lines to set.
     */
    void setLines(int page, List<SKJHoloLine> lines);

    /**
     * Sets all the pages of the hologram if the provider supports this.
     * @param pages the pages to set.
     */
    void setPages(Map<Integer, List<SKJHoloLine>> pages);

    /**
     * Shows the hologram to the given players.
     * @param page the page to show.
     * @param players players to show the hologram to.
     */
    void showTo(int page, Player[] players);

    /**
     * Hides the hologram from the given players.
     * @param players players to hide the hologram from.
     */
    void hideFrom(Player[] players);

    /**
     * Returns whether the given player can see the hologram.
     * @param player the player to check.
     * @return true if the player can see.
     */
    boolean canSee(Player player);

    /**
     * Destroys and removes the hologram.
     * Please ensure in all implementations you remove the key from the cache.
     */
    void delete();

    /**
     * Returns whether to save this hologram over restart.
     * Please note not all providers support this.
     * @return whether the hologram should be saved over restart.
     */
    boolean isPersistent();

    /**
     * Sets whether to make this hologram save over restart.
     * Please note not all providers support this.
     * @param persistent true if it should be saved over restart.
     */
    void setPersistent(boolean persistent);

    /**
     * Returns whether the hologram will not always face the player.
     * Please note not all providers support this.
     * @return whether the hologram will not always face players.
     */
    boolean isStatic();

    /**
     * Sets if the holograms will not always face players.
     * @param stat false if the holograms should face the player.
     */
    void setStatic(boolean stat);

    /**
     * Gets all the pages of a hologram.
     * @return the pages.
     */
    Map<Integer, List<SKJHoloLine>> getPages();

    /**
     * Gets the lines of a page.
     * @param page the page index.
     * @return the lines of a page.
     */
    List<SKJHoloLine> getPage(int page);

    /**
     * Gets the index of a viewed page for a player.
     * @param player the player to check.
     * @return the index.
     */
    int getPage(Player player);

    /**
     * Gets the location of the hologram.
     * @return the location.
     */
    Location getLocation();

    /**
     * Gets the internal hologram of the current SKJHolo.
     * @return the internal hologram.
     */
    Object getHologram();

}
