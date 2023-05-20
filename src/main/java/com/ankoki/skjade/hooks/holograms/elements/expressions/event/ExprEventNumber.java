package com.ankoki.skjade.hooks.holograms.elements.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// This is used to get either event-page or event-line.
public class ExprEventNumber extends SimpleExpression<Number> {

	private static final HoloProvider PROVIDER = HoloHandler.get().getCurrentProvider();
	private static final boolean SUPPORTS_CLICK = PROVIDER.supportsOnClick(false),
								SUPPORTS_LINE_CLICK = PROVIDER.supportsOnClick(true),
								SUPPORTS_PAGES = PROVIDER.supportsPages();

	static {
		if (SUPPORTS_CLICK) {
			List<String> patterns = new ArrayList<>();
			if (SUPPORTS_LINE_CLICK)
				patterns.add("[event(-| )]line[(-| )index]");
			if (SUPPORTS_PAGES)
				patterns.add("[event(-| )]page[(-| )index]");
			Skript.registerExpression(ExprEventNumber.class, Number.class, ExpressionType.SIMPLE, patterns.toArray(new String[0]));
		}
	}

	private boolean isLine;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		isLine = SUPPORTS_LINE_CLICK && i == 0;
		if (!getParser().isCurrentEvent(HologramInteractEvent.class)) {
			Skript.error("You cannot use event-" + (isLine ? "line" : "page") + " outside a hologram interact event.");
			return false;
		}
		return true;
	}

	@Override
	protected @Nullable Number[] get(Event event) {
		assert event instanceof HologramInteractEvent : "Illegal Access: Event is not instanceof HologramInteractEvent.";
		return new Number[]{isLine ? ((HologramInteractEvent) event).getLine() : ((HologramInteractEvent) event).getPage()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "event-" + (isLine ? "line" : "page");
	}

}
