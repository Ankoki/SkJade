package com.ankoki.skjade.hooks.holograms.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.*;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

@Name("Hologram Interaction")
@Description({"Declares the interaction of certain lines/pages for a hologram.",
				"Hologram plugins may not support this feature so be wary.",
				"If a plugin does not support the clicking of individual lines, you may declare the page for " +
				"some plugins.",
				"If the provider does not support pages, then you can ignore the page declaration."})
@Examples({"create new holo keyed \"i've been known to kiss and tell\":",
		"\tpage 0: \"send girls to wishing wells\", \"if you're my man i want you to myself\", glowing diamond sword and an ender dragon",
		"\tpersistent: true",
		"\tstatic: false",
		"\ton right shift click of page 0:",
		"\t\tsend \"Hey! You clicked %event-hologram's id%!\"",
		"\t\twait 3 seconds",
		"\t\tsend \"Bye bye!\"",
		"\t\tdelete event-hologram"})
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class SecInteraction extends Section {

	private static boolean supportsLineClick, supportsPageClick;

	static {
		HoloProvider provider = HoloHandler.get().getCurrentProvider();
		SecInteraction.supportsLineClick = provider.supportsOnClick(true);
		SecInteraction.supportsPageClick = provider.supportsOnClick(false);
		if (supportsLineClick)
			Skript.registerSection(SecInteraction.class,
					"on [:shift] [:left|:right] click[ing] [[of|on] line %-numbers%] [[of|on] page %-numbers%]");
		else if (supportsPageClick)
			Skript.registerSection(SecInteraction.class,
					"on [:shift] (:left|:right|) click[[ing] [page: [on|of] page %-number%]]");
	}

	private ClickType clickType;
	private Expression<Number> lineExpr, pageExpr;
	private Trigger trigger;
	private SecKeyedHologram section;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		SkriptEvent event = this.getParser().getCurrentSkriptEvent();
		if (!(event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class))) {
			Skript.error("You must be creating a hologram to specify interactions.");
			return false;
		}
		section = (SecKeyedHologram) skriptEvent.getSection();
		final boolean left = parseResult.hasTag("left");
		final boolean right = parseResult.hasTag("right");
		final boolean shift = parseResult.hasTag("shift");
		if (left && shift) clickType = ClickType.SHIFT_LEFT;
		else if (left) clickType = ClickType.LEFT;
		else if (right && shift) clickType = ClickType.SHIFT_RIGHT;
		else if (right) clickType = ClickType.RIGHT;
		else clickType = ClickType.ANY;
		if (supportsLineClick) lineExpr = (Expression<Number>) exprs[0];
		pageExpr = (Expression<Number>) exprs[supportsLineClick ? 1 : 0];
		this.trigger = loadCode(sectionNode, "hologram interaction", HologramInteractEvent.class);
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		Number[] lineNumbers = lineExpr == null ? new Number[0] : lineExpr.getAll(event);
		Number[] pageNumbers = pageExpr == null ? new Number[0] : pageExpr.getAll(event);
		int[] lines = new int[lineNumbers.length];
		int i = 0;
		for (Number number : lineNumbers) {
			if (number != null && number.intValue() != -1) {
				lines[i] = number.intValue();
				i++;
			}
		}
		int[] pages = new int[pageNumbers.length];
		i = 0;
		for (Number number : pageNumbers) {
			if (number != null && number.intValue() != -1) {
				pages[i] = number.intValue();
				i++;
			}
		}
		SKJHoloBuilder builder = section.getCurrentBuilder();
		builder.addInteraction(new HologramTrigger(lines, pages, clickType, trigger));
		section.setCurrentBuilder(builder);
		return this.getNext();
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "on " + clickType.toString() + "click" + (lineExpr == null ? "" : " on line " + lineExpr.toString(event, b)) +
				(pageExpr == null ? "" : " of page " + pageExpr.toString(event, b));
	}
}
