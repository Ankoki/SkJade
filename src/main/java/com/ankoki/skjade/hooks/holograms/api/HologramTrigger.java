package com.ankoki.skjade.hooks.holograms.api;

import ch.njol.skript.lang.Trigger;
import com.ankoki.roku.misc.NumberUtils;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HologramTrigger {

	private final int[] pages;
	private final int[] lines;
	private final ClickType type;
	private final Trigger trigger;

	public HologramTrigger(int[] pages,
						   int[] lines,
						   ClickType type,
						   Trigger trigger) {
		this.pages = pages;
		this.lines = lines;
		this.type = type;
		this.trigger = trigger;
	}

	public void execute(int page, int line, ClickType type, HologramInteractEvent event) {
		if (event.isCancelled())
			return;
		SkJade.getInstance().getLogger().warning("DEBUG | PAGE - " + page);
		SkJade.getInstance().getLogger().warning("DEBUG | PAGES ACCEPTED - " +
				Arrays.stream(pages).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
		SkJade.getInstance().getLogger().warning("DEBUG | LINE - " + line);
		SkJade.getInstance().getLogger().warning("DEBUG | TYPE - " + type.name());
		if (this.allowedType(type)) {
			SkJade.getInstance().getLogger().warning("DEBUG | TYPE IS ANY OR TYPE");
			if (!HoloHandler.get().getCurrentProvider().supportsPages() ||
					pages.length == 0 ||
					NumberUtils.arrayContains(pages, page)) {
				SkJade.getInstance().getLogger().warning("DEBUG | PAGE IS CONTAINED OR LENGTH OF PAGES IS 0");
				if (!HoloHandler.get().getCurrentProvider().supportsOnClick(true) ||
						lines.length == 0 ||
						NumberUtils.arrayContains(lines, line)) {
					SkJade.getInstance().getLogger().warning("DEBUG | LINE IS NOT SUPPORTED, EXECUTING");
					trigger.execute(event);
				}
			}
		}
	}

	private boolean allowedType(ClickType type) {
		if (type == ClickType.ANY)
			return true;
		else if (type == ClickType.ANY_LEFT && (this.type == ClickType.LEFT || this.type == ClickType.SHIFT_LEFT))
			return true;
		else if (type == ClickType.ANY_RIGHT && (this.type == ClickType.RIGHT || this.type == ClickType.SHIFT_RIGHT))
			return true;
		else if (type == ClickType.ANY_SHIFT && (this.type == ClickType.SHIFT_LEFT || this.type == ClickType.SHIFT_RIGHT))
			return true;
		return this.type == type;
	}

	public int[] getPages() {
		return pages;
	}

	public int[] getLines() {
		return lines;
	}

	public ClickType getType() {
		return type;
	}

	public Trigger getTrigger() {
		return trigger;
	}
}
