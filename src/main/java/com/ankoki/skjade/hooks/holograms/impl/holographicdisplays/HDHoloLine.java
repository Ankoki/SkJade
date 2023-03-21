package com.ankoki.skjade.hooks.holograms.impl.holographicdisplays;

import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HDHoloLine implements SKJHoloLine {

	private final int index;
	private final String content, text;
	private final Material material;
	private final ItemStack item;

	/**
	 * Parses an object into a hologram line.
	 * @param line the line to parse.
	 */
	public HDHoloLine(Object line) {
		this(line, -1);
	}

	/**
	 * Parses an object into a hologram line.
	 * @param line the line to parse.
	 * @param index the index, -1 if not applicable.
	 */
	public HDHoloLine(Object line, int index) {
		this.index = index;
		if (line instanceof Material material) {
			this.content = material.name();
			this.text = null;
			this.material = material;
			this.item = new ItemStack(material);
		} else if (line instanceof ItemStack item) {
			this.content = item.getType().name();
			this.text = null;
			this.material = item.getType();
			this.item = item;
		} else if (line instanceof HologramLine holoLine) {
			if (holoLine instanceof TextLine textLine) {
				this.content = textLine.getText();
				this.text = textLine.getText();
				this.material = null;
				this.item = null;
			} else {
				ItemLine itemLine = (ItemLine) line;
				this.content = itemLine.getItemStack().getType().name();
				this.text = null;
				this.material = itemLine.getItemStack().getType();
				this.item = itemLine.getItemStack();
			}
		} else {
			this.content = String.valueOf(line);
			this.text = String.valueOf(line);
			this.material = null;
			this.item = null;
		}
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
		return null;
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
