package com.ankoki.skjade.hooks.holograms.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloManager;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
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
		HoloProvider provider = HoloManager.get().getCurrentProvider();
		SecInteraction.supportsLineClick = provider.supportsOnClick(true);
		SecInteraction.supportsPageClick = provider.supportsOnClick(false);
		if (supportsLineClick) {
			Skript.registerSection(SecInteraction.class,
					"on (:left|:right|) [:shift] click[ing] (of|on|) [line: line %number%] [page: (of|on|) page %number%]");
		} else if (supportsPageClick) {
			Skript.registerSection(SecInteraction.class,
					"on (:left|:right|) [:shift] click[ing] [page: [on] page %number%]");
		}
	}

	private Kleenean clickType;
	private boolean shift, line, page;
	private Expression<Number> lineExpr, pageExpr;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		return null;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return null;
	}
}
