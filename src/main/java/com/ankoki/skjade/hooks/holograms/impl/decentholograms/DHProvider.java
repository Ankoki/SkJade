package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerSection;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.*;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;
import com.ankoki.skjade.hooks.holograms.elements.effects.EffPlaceholderReturn;
import eu.decentsoftware.holograms.api.actions.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DHProvider implements HoloProvider {

	private static final DHProvider INSTANCE = new DHProvider();

	public static DHProvider get() {
		return INSTANCE;
	}

	@Override
	public SKJHoloLine parseLine(Object line) {
		return new DHHoloLine(line);
	}

	@Override
	public List<SKJHoloLine> parseLines(List<Object> lines) {
		List<SKJHoloLine> parsed = new ArrayList<>();
		for (Object object : lines)
			parsed.add(new DHHoloLine(object));
		return parsed;
	}

	@Override
	public ClickType parseClickType(Object click) {
		if (click instanceof String string)
			return ClickType.valueOf(string);
		else if (click instanceof Enum<?> en)
			return ClickType.valueOf(en.name());
		else
			return null;
	}

	@Override
	public @NotNull String getId() {
		return "DecentHolograms";
	}

	@Override
	public boolean supportsPages() {
		return true;
	}

	@Override
	public boolean supportsOnClick(boolean singleLine) {
		return !singleLine;
	}

	@Override
	public boolean supportsOnTouch() {
		return false;
	}

	@Override
	public boolean supportsPersistence() {
		return true;
	}

	@Override
	public boolean supportsStatic() {
		return true;
	}

	@Override
	public boolean supportsPerPlayer() {
		return true;
	}

	@Override
	public boolean supportsEntityLines() {
		return true;
	}

	@Override
	public boolean supportsCustomPlaceholders() {
		return false;
	}

	@Override
	public void registerPlaceholder(String name, int refreshRate, Trigger trigger, Structure structure) {}

	@Override
	public @Nullable SKJHolo createHolo(String name, Location location, Map<Integer, List<SKJHoloLine>> pages) {
		return DHHolo.create(name, location, pages);
	}

	@Override
	public void setup() {
		new ActionType("SKJADE") {
			public boolean execute(Player player, String... args) {
				if (args.length != 1)
					return false;
				// args = "holoname.pageindex.clicktype"
				args = args[0].split("\\.");
				Event call = new HologramInteractEvent(player, HoloHandler.get().getHolo(args[0]), Integer.parseInt(args[1]), -1, parseClickType(args[2]));
				Bukkit.getScheduler().runTask(SkJade.getInstance(), () -> Bukkit.getPluginManager().callEvent(call));
				return true;
			}
		};
	}

	@Override
	public SKJHolo getHologramFrom(Object object) {
		for (SKJHolo holo : HoloHandler.get().getHolograms()) {
			if (holo.getHologram() == object)
				return holo;
		} return null;
	}

}
