package com.ankoki.skjade.hooks.holograms.api;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SKJHoloLine {

    /**
     * Transforms a list of lines into an object array.
     * @param lines the lines to transform.
     * @return the new array.
     */
    static Object[] transform(List<SKJHoloLine> lines) {
        Object[] changed = new Object[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            SKJHoloLine line = lines.get(i);
            Type type = Type.getType(line);
            switch (type) {
                case TEXT -> changed[i] = line.getText();
                case ITEM -> changed[i] = line.getItem();
                case MATERIAL -> changed[i] = line.getMaterial();
                case ENTITY -> changed[i] = line.getEntity();
                case EMPTY -> changed[i] = null;
            }
        }
        return changed;
    }

    /**
     * Gets the index of the line, or -1 if transformed from objects.
     * @return the index of a line.
     */
    int getIndex();

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

    /**
     * Gets the current line contents as an object.
     * @return the current line contents.
     */
    @Nullable Object get();

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
