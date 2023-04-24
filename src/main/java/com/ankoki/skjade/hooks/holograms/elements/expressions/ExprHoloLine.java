package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Hologram")
@Description("Gets the line of a hologram from its index or page.")
@Examples("set {_line} to line 5 of holo keyed as \"shut down by blackpink\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprHoloLine extends SimpleExpression<SKJHoloLine> {

	private static final boolean SUPPORTS_PAGES = HoloHandler.get().getCurrentProvider().supportsPages();

	static {
		String[] patterns = new String[SUPPORTS_PAGES ? 4 : 2];
		patterns[0] = "[all ]lines (in|for|of) %skjholo%";
		patterns[1] = "line %number% (in|for|of) %skjholo%";
		if (SUPPORTS_PAGES) {
			patterns[2] = "[all lines of ]page %number% (in|for|of) %skjholo%";
			patterns[3] = "line %number% of page %number% (in|for|of) %skjholo%";
		}
		Skript.registerExpression(ExprHoloLine.class, SKJHoloLine.class, ExpressionType.COMBINED, patterns);
	}

	private Expression<Number> lineExpr, pageExpr;
	private Expression<SKJHolo> holoExpr;
	private boolean allLines;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		allLines = i == 0 || i == 2;
		holoExpr = (Expression<SKJHolo>) exprs[allLines ? 0 : (SUPPORTS_PAGES ? 2 : 1)];
		lineExpr = allLines ? null : (Expression<Number>) exprs[0];
		pageExpr = allLines ? null : (SUPPORTS_PAGES ? (Expression<Number>) exprs[1] : null);
		return true;
	}

	@Override
	protected @Nullable SKJHoloLine[] get(Event event) {
		final SKJHolo holo = holoExpr.getSingle(event);
		if (holo == null)
			return new SKJHoloLine[0];
		int lineNumber = 0;
		if (lineExpr != null) {
			Number line = lineExpr.getSingle(event);
			if (line == null)
				return new SKJHoloLine[0];
			else 
				lineNumber = line.intValue();
		}
		int pageIndex = 0;
		if (pageExpr != null) {
			Number page = pageExpr.getSingle(event);
			if (page == null)
				return new SKJHoloLine[0];
			else 
				pageIndex = page.intValue();
		}
		final List<SKJHoloLine> page = holo.getPage(pageIndex);
		if (allLines)
			return page.toArray(new SKJHoloLine[0]);
		else if (page.size() < lineNumber)
			return new SKJHoloLine[]{holo.getPage(pageIndex).get(lineNumber)};
		else
			return new SKJHoloLine[0];
	}


	@Override
	public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
		return CollectionUtils.array(Object.class);
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (delta.length == 0 && (mode == ChangeMode.RESET))
			return;
		if ((delta[0] instanceof EntityType || delta[0] instanceof Entity) && 
				!HoloHandler.get().getCurrentProvider().supportsEntityLines())
			return;
		SKJHolo holo = holoExpr.getSingle(event);
		if (holo == null)
			return;
		int page = 0;
		if (pageExpr != null) {
			Number number = pageExpr.getSingle(event);
			if (number == null)
				return;
			page = HoloHandler.get().getCurrentProvider().supportsPages() ? number.intValue() : 0;
		}
		switch (mode) {
			case ADD -> holo.appendLine(page, HoloHandler.get().getCurrentProvider().parseLine(delta[0]));
			case SET -> holo.setLines(page, HoloHandler.get().getCurrentProvider().parseLines(Arrays.asList(delta)));
			case REMOVE -> {
				List<SKJHoloLine> current = holo.getPage(page);
				List<SKJHoloLine> updated = new ArrayList<>();
				Object object = delta[0];
				for (SKJHoloLine line : current)
					if (line.get() != object)
						updated.add(line);
				holo.setLines(page, updated);
			}
			case DELETE -> holo.delete();
			default -> holo.setLines(page, List.of());
		}
	}

	@Override
	public boolean isSingle() {
		return !allLines;
	}

	@Override
	public Class<? extends SKJHoloLine> getReturnType() {
		return SKJHoloLine.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return allLines ?
				(pageExpr == null ? "all lines of " + holoExpr.toString(event, debug) :
						"all lines of page " + pageExpr.toString(event, debug) + " in " + holoExpr.toString(event, debug)) :
				(pageExpr == null ? "line " + lineExpr.toString(event, debug) + " of " + holoExpr.toString(event, debug) :
						"line " + lineExpr.toString(event, debug) + " of " + pageExpr.toString(event, debug) + " in " + holoExpr.toString(event,debug));
	}

}
