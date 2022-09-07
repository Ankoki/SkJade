package com.ankoki.skjade.hooks.holograms.elements.sections;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

public class SecInteraction extends Section {

	static {

	}

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		return false;
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
