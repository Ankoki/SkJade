package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.bukkitutil.EntityUtils;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.api.utils.entity.HologramEntity;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DHHoloLine implements SKJHoloLine {

    /**
     * Parses objects into hologram lines.
     *
     * @param lines the objects to parse.
     * @return the parsed lines.
     */
    public static List<SKJHoloLine> parseLines(List<Object> lines) {
        List<SKJHoloLine> parsed = new ArrayList<>();
        for (Object object : lines)
            parsed.add(new DHHoloLine(object));
        return parsed;
    }

    /**
     * Parses DH hologram lines into SkJade hologram lines.
     *
     * @param lines the DH hologram lines to parse.
     * @return the parsed lines.
     */
    public static List<SKJHoloLine> parseDHLines(List<HologramLine> lines) {
        List<SKJHoloLine> parsed = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            switch (line.getType()) {
                case ENTITY -> parsed.add(new DHHoloLine(line.getEntity()));
                case TEXT -> parsed.add(new DHHoloLine(line.getText()));
                case HEAD, SMALLHEAD, ICON -> parsed.add(new DHHoloLine(line.getItem()));
                case UNKNOWN -> parsed.add(new DHHoloLine(line.getContent()));
            }
            parsed.add(new DHHoloLine(line.getType(), i));
        }
        return parsed;
    }

    private final String content;
    private final String text;
    private final Material material;
    private final ItemStack item;
    private final EntityType entity;
    private final int index;

    /**
     * Parses an object into a hologram line.
     * @param line the line to parse.
     */
    public DHHoloLine(Object line) {
        this(line, -1);
    }

    /**
     * Parses an object into a hologram line.
     * @param line the line to parse.
     * @param index the index, -1 if not applicable.
     */
    public DHHoloLine(Object line, int index) {
        if (line instanceof Material || line instanceof ItemStack) {
            ItemStack item = line instanceof Material ? new ItemStack((Material) line) : (ItemStack) line;
            this.content = "#ICON:" + HologramItem.fromItemStack(item).getContent();
            this.text = null;
            this.material = line instanceof Material material ? material : null;
            this.item = item;
            this.entity = null;
        } else if (line instanceof ItemType item) {
            this.content = "#ICON:" + HologramItem.fromItemStack(item.getRandom()).getContent();
            this.text = null;
            this.material = null;
            this.item = item.getRandom();
            this.entity = null;
        } else if (line instanceof HologramItem item) {
            this.content = item.getContent();
            this.text = content;
            this.material = item.getMaterial();
            this.item = item.parse();
            this.entity = null;
        } else if (line instanceof HologramEntity entity) {
            EntityType type = entity.getType();
            this.content = "#ENTITY:" + (DecentEntityType.isAllowed(type) ? type.name() : "PIG");
            this.text = content;
            this.material = null;
            this.item = null;
            this.entity = type;
        } else if (line instanceof EntityType || line instanceof Entity || line instanceof ch.njol.skript.entity.EntityType) {
            EntityType type;
            if (line instanceof ch.njol.skript.entity.EntityType skriptType) type = EntityUtils.toBukkitEntityType(skriptType.data);
            else type = line instanceof Entity entity ? entity.getType() : (EntityType) line;
            this.content = "#ENTITY:" + (DecentEntityType.isAllowed(type) ? type.name() : "PIG");
            this.text = content;
            this.material = null;
            this.item = null;
            this.entity = type;
        } else {
            this.content = String.valueOf(line);
            this.text = content;
            this.material = null;
            this.item = null;
            this.entity = null;
        }
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public @NotNull String getContent() {
        return content;
    }

    @Override
    public @Nullable String getText() {
        return text;
    }

    @Override
    public @Nullable Material getMaterial() {
        return material;
    }

    @Override
    public @Nullable ItemStack getItem() {
        return item;
    }

    @Override
    public @Nullable EntityType getEntity() {
        return entity;
    }

    @Override
    public @Nullable Object get() {
        Type type = Type.getType(this);
        return switch (type) {
            case TEXT -> this.getText();
            case ITEM -> this.getItem();
            case MATERIAL -> this.getMaterial();
            case ENTITY -> this.getEntity();
            default -> null;
        };
    }

}
