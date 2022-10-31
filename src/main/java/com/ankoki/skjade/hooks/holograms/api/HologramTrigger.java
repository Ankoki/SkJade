package com.ankoki.skjade.hooks.holograms.api;

import ch.njol.skript.lang.Trigger;
import com.ankoki.roku.misc.NumberUtils;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;
import org.bukkit.event.Event;

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
		if (pages.length == 0 || NumberUtils.arrayContains(pages, page)) {
			if (lines.length == 0 || NumberUtils.arrayContains(lines, line)) {
				if (this.type == type)
					trigger.execute(event);
			}
		}
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
