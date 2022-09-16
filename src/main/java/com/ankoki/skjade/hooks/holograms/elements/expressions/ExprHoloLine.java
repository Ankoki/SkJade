package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram")
@Description("Gets the line of a hologram from its index, or all lines.")
@Examples("set {_line} to line 5 of holo keyed as \"shut down by blackpink\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class ExprHoloLine extends SimpleExpression<SKJHoloLine> {

	private static final boolean SUPPORTS_PAGES = HoloHandler.get().getCurrentProvider().supportsPages();

	static {
		String[] patterns = new String[SUPPORTS_PAGES ? 4 : 2];
		patterns[0] = "all lines of %skjholo%";
		patterns[1] = "line %number% of %skjholo%";
		if (SUPPORTS_PAGES) {
			patterns[2] = "all lines of page %number% in %skjholo%";
			patterns[3] = "line %number% of page %number% in %skjholo%";
		}
		Skript.registerExpression(ExprHoloLine.class, SKJHoloLine.class, ExpressionType.SIMPLE, patterns);
	}

	private Expression<Number> lineExpr, pageExpr;
	private Expression<SKJHolo> holoExpr;
	private boolean allLinesSingle, allLinesPage;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		allLinesSingle = i == 0;
		holoExpr = (Expression<SKJHolo>) exprs[allLinesSingle ? 0 : (SUPPORTS_PAGES ? 2 : 1)];
		lineExpr = allLinesSingle ? null : (Expression<Number>) exprs[0];
		pageExpr = allLinesSingle ? null : (SUPPORTS_PAGES ? (Expression<Number>) exprs[1] : null);
		return true;
	}

	@Override
	protected @Nullable SKJHoloLine[] get(Event event) {
		final SKJHolo holo = holoExpr.getSingle(event);
		if (holo == null) return new SKJHoloLine[0];
		if (allLinesSingle) return holo.getPage(0).toArray(new SKJHoloLine[0]);
		return new SKJHoloLine[0];
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends SKJHoloLine> getReturnType() {
		return null;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return null;
	}
}
