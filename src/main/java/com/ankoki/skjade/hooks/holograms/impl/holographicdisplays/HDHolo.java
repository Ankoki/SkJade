package com.ankoki.skjade.hooks.holograms.impl.holographicdisplays;

import ch.njol.skript.Skript;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HDHolo implements SKJHolo {

	private final String key;
	private final Hologram current;

	public static HDHolo create(String key, Location location, Map<Integer, List<SKJHoloLine>> pages) {
		if (HoloHandler.get().getHolo(key) != null) {
			Skript.error("The given key already exists. Hologram will not be created.");
			return null;
		}
		return new HDHolo(key, location, pages);
	}

	private HDHolo(String key, Location location, Map<Integer, List<SKJHoloLine>> pages) {
		this.key = key;
		this.current = HologramsAPI.createHologram(SkJade.getInstance(), location);
		this.setPages(pages);
		this.register(key);
	}

	@Override
	public @NotNull String getKey() {
		return key;
	}

	@Override
	public void teleport(Location location) {
		this.current.teleport(location);
	}

	@Override
	public void appendLine(int page, SKJHoloLine line) {
		if (line.getItem() != null)
			this.current.appendItemLine(line.getItem());
		else
			this.current.appendTextLine(line.getContent());
	}

	@Override
	public void setLine(int page, int index, SKJHoloLine line) {
		HologramLine l = current.getLine(index);
		if (l instanceof ItemLine itemLine)
			itemLine.setItemStack(line.getItem() == null ? new ItemStack(Material.STONE) : line.getItem());
		else if (l instanceof TextLine textLine)
			textLine.setText(line.getContent());
	}

	@Override
	public void setLines(int page, List<SKJHoloLine> lines) {
		for (int i = 0; i < lines.size(); i++)
			this.setLine(-1, i, lines.get(i));
	}

	@Override
	public void setPages(Map<Integer, List<SKJHoloLine>> pages) {
		this.setLines(-1, pages.get(0));
	}

	@Override
	public void showTo(int page, Player[] players) {
		for (Player player : players)
			current.getVisibilityManager().showTo(player);
	}

	@Override
	public void hideFrom(Player[] players) {
		for (Player player : players)
			current.getVisibilityManager().hideTo(player);
	}

	@Override
	public boolean canSee(Player player) {
		return this.current.getVisibilityManager().isVisibleTo(player);
	}

	@Override
	public void destroy() {
		this.current.delete();
		HoloHandler.get().deleteHolo(key);
	}

	@Override
	public boolean isPersistent() {
		return false;
	}

	@Override
	public void setPersistent(boolean persistent) {}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public void setStatic(boolean stat) {}

	@Override
	public Map<Integer, List<SKJHoloLine>> getPages() {
		List<SKJHoloLine> lines = new ArrayList<>();
		for (int i = 0; true; i++) {
			HologramLine line = current.getLine(i);
			if (line == null)
				break;
			else
				lines.add(new HDHoloLine(line, i));
		}
		Map<Integer, List<SKJHoloLine>> map = new HashMap<>();
		map.put(0, lines);
		return map;
	}

	@Override
	public List<SKJHoloLine> getPage(int page) {
		return this.getPages().get(0);
	}

	@Override
	public Location getLocation() {
		return current.getLocation();
	}

	@Override
	public Object getHologram() {
		return current;
	}
}
