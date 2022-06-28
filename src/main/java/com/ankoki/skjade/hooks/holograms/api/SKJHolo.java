package com.ankoki.skjade.hooks.holograms.api;

import com.ankoki.skjade.hooks.holograms.HoloManager;
import org.bukkit.Location;

import java.util.List;

public interface SKJHolo {

    /**
     * Registers the hologram under the given key.
     * @param key the key to register.
     * @return true if successful.
     */
    default boolean register(String key) {
         return HoloManager.get().registerHolo(key, this);
    }

    /**
     * Moves the hologram to given location.
     * @param location the location to move the hologram to.
     */
    void teleport(Location location);

    /**
     * Appends a line to the hologram.
     * @param line the line to add.
     */
    void appendLine(SKJHoloLine line);

    /**
     * Sets a line to the text.
     * @param index the index to set.
     * @param line the text to set it to.
     */
    void setLine(int index, SKJHoloLine line);

    /**
     * Sets the lines of the hologram.
     * @param lines the lines to set.
     */
    void setLines(List<SKJHoloLine> lines);

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
     * Destroys and removes the hologram.
     */
    void destroy();
}
