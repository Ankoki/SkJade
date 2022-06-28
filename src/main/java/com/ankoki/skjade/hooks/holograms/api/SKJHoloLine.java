package com.ankoki.skjade.hooks.holograms.api;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SKJHoloLine {

    /**
     * Gets the content of the line.
     * @return the contents.
     */
    @NotNull String getContent();

    /**
     * Gets the text of the line, if the line has one.
     * @return the text, null if that is not the line type.
     */
    @Nullable String getText();

    /**
     * Gets the material of the line, if the line has one.
     * @return the material, null if that is not the line type.
     */
    @Nullable Material getMaterial();

    /**
     * Gets the item of the line, if the line has one.
     * @return the item, null if that is not the line type.
     */
    @Nullable ItemStack getItem();

    /**
     * Gets the entity of the line, if the line has one.
     * @return the entity, null if that is not the line type.
     */
    @Nullable EntityType getEntity();

    enum Type {
        TEXT, ITEM, MATERIAL, ENTITY, EMPTY;

        /**
         * Gets the type of a line.
         * @param line the line to search.
         * @return the type of line.
         */
        public static Type getType(SKJHoloLine line) {
            if (line.getText() != null) return TEXT;
            if (line.getMaterial() != null) return MATERIAL;
            if (line.getItem() != null) return ITEM;
            if (line.getEntity() != null) return ENTITY;
            return EMPTY;
        }
    }
}
