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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Page")
@Description("Gets the page of a hologram a player is viewing. Only some hologram plugins support this.")
@Examples("set {_page} to page viewed of {_holo} by player")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class ExprHoloPage extends SimpleExpression<Number> {

	static {
		if (HoloHandler.get().getCurrentProvider().supportsPages())
			Skript.registerExpression(ExprHoloPage.class, Number.class, ExpressionType.COMBINED,
					"[the] [viewed] page (index|number) of %skjholo% for %player%");
	}

	private Expression<SKJHolo> holoExpr;
	private Expression<Player> playerExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		holoExpr = (Expression<SKJHolo>) exprs[0];
		playerExpr = (Expression<Player>) exprs[1];
		return true;
	}

	@Override
	protected Number[] get(Event event) {
		SKJHolo holo = holoExpr.getSingle(event);
		Player player = playerExpr.getSingle(event);
		if (holo == null || player == null)
			return new Number[0];
		return new Number[]{holo.getPage(player)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Integer.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "page index of " + holoExpr.toString(event, debug) + " for " + playerExpr.toString(event, debug);
	}

}
