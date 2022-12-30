package com.ankoki.skjade.hooks.holograms.elements.structures;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.util.ContextlessEvent;
import ch.njol.skript.util.Timespan;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.lang.entry.util.TriggerEntryData;
import org.skriptlang.skript.lang.structure.Structure;

public class StructPlaceholder extends Structure {

	static {
		if (HoloHandler.get().getCurrentProvider().supportsCustomPLaceholders())
			Skript.registerStructure(StructPlaceholder.class,
					EntryValidator.builder()
							.addEntryData(new ExpressionEntryData<>("name", null, false, String.class, ContextlessEvent.class))
							.addEntryData(new ExpressionEntryData<>("refresh rate", null, true, Timespan.class, ContextlessEvent.class))
							.addEntryData(new TriggerEntryData("trigger", null, false, ContextlessEvent.class))
							.build(),
					"register [custom] [holo[gram]] placeholder");
	}

	@Override
	public boolean init(Literal<?>[] literals, int i, ParseResult parseResult, EntryContainer entryContainer) {
		return true;
	}

	@Override
	public boolean load() {
		EntryContainer container = this.getEntryContainer();
		String name = container.get("name", String.class, false);
		Timespan refresh = container.getOptional("refresh rate", Timespan.class, false);
		if (refresh == null)
			refresh = new Timespan(500); // Default refresh rate sits at 1 second, as you cannot go any lower.
		Trigger trigger = container.get("trigger", Trigger.class, false);
		HoloHandler.get().getCurrentProvider().registerPlaceholder(name, (int) Math.max(1, refresh.getTicks_i() * 20), trigger, this);
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "register custom hologram placeholder";
	}

}
