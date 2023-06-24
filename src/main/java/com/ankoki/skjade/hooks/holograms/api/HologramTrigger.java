package com.ankoki.skjade.hooks.holograms.api;

import ch.njol.skript.lang.Trigger;
import com.ankoki.roku.misc.NumberUtils;

import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;

public class HologramTrigger {

	private final int[] pages;
	private final int[] lines;
	private final ClickType type;
	private final Trigger trigger;

	/**
	 * Creates a new hologram trigger for the given criteria.
	 *
	 * @param pages the page numbers needed.
	 * @param lines the line numbers needed.
	 * @param type the type of click needed.
	 * @param trigger the trigger to execute if the criteria is met.
	 */
	public HologramTrigger(int[] pages,
						   int[] lines,
						   ClickType type,
						   Trigger trigger) {
		this.pages = pages;
		this.lines = lines;
		this.type = type;
		this.trigger = trigger;
	}

	/**
	 * Attempts to execute the trigger with the given line, page and click.
	 *
	 * @param page the page clicked.
	 * @param line the line clicked.
	 * @param type the click type.
	 * @param event the event it is under to execute.
	 */
	public void execute(int page, int line, ClickType type, HologramInteractEvent event) {
		if (event.isCancelled())
			return;
		if (this.allowedType(type)) {
			if (!HoloHandler.get().getCurrentProvider().supportsPages() ||
					pages.length == 0 ||
					NumberUtils.arrayContains(pages, page)) {
				if (!HoloHandler.get().getCurrentProvider().supportsOnClick(true) ||
						lines.length == 0 ||
						NumberUtils.arrayContains(lines, line)) {
					trigger.execute(event);
				}
			}
		}
	}

	/**
	 * Checks if the required click type falls under the given type.
	 *
	 * @param type the click type.
	 * @return true if allowed.
	 */
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

	/**
	 * Gets the pages this can be executed in.
	 *
	 * @return the pages.
	 */
	public int[] getPages() {
		return pages;
	}

	/**
	 * Gets the lines this can be executed on.
	 *
	 * @return the lines.
	 */
	public int[] getLines() {
		return lines;
	}

	/**
	 * Gets the click type this can be executed with.
	 *
	 * @return the click type.
	 */
	public ClickType getType() {
		return type;
	}

	/**
	 * Gets the trigger to be executed.
	 *
	 * @return the trigger.
	 */
	public Trigger getTrigger() {
		return trigger;
	}

}
