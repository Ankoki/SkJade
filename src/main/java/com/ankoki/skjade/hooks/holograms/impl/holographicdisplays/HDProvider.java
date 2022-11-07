package com.ankoki.skjade.hooks.holograms.impl.holographicdisplays;

import com.ankoki.skjade.hooks.holograms.api.*;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HDProvider implements HoloProvider {

	@Override
	public SKJHoloLine parseLine(Object line) {
		return new HDHoloLine(line);
	}

	@Override
	public List<SKJHoloLine> parseLines(List<Object> lines) {
		List<SKJHoloLine> parsed = new ArrayList<>();
		for (Object line : lines)
			parsed.add(this.parseLine(line));
		return parsed;
	}

	@Override
	public ClickType parseClickType(Object click) {
		return null;
	}

	@Override
	public @NotNull String getId() {
		return "HolographicDisplays";
	}

	@Override
	public boolean supportsPages() {
		return false;
	}

	@Override
	public boolean supportsOnClick(boolean singleLine) {
		return true;
	}

	@Override
	public boolean supportsOnTouch() {
		return true;
	}

	@Override
	public boolean supportsPersistence() {
		return false;
	}

	@Override
	public boolean supportsStatic() {
		return false;
	}

	@Override
	public boolean supportsPerPlayer() {
		return true;
	}

	@Override
	public boolean supportsEntityLines() {
		return false;
	}

	@Override
	public @Nullable SKJHolo createHolo(String name, Location location, Map<Integer, List<SKJHoloLine>> pages) {
		return HDHolo.create(name, location, pages);
	}

	@Override
	public SKJHolo getHologramFrom(Object object) {
		for (SKJHolo holo : HoloHandler.get().getHolograms()) {
			if (holo.getHologram() == object)
				return holo;
		} return null;
	}
}