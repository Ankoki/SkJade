package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Visibility")
@Description("Gets a hologram's visibility for certain players.")
@Examples("set {_x} to visibility of {_holo} for {-admin::%arg-1%}")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class ExprVisibility extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprVisibility.class, Boolean.class, ExpressionType.SIMPLE,
				"%skjholo%'s visibility for %player%",
				"visibility of %skjholo% for %player%");
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
	protected @Nullable Boolean[] get(Event event) {
		Player player = playerExpr.getSingle(event);
		SKJHolo holo = holoExpr.getSingle(event);
		if (player == null || holo == null) return new Boolean[0];
		return new Boolean[]{holo.canSee(player)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return holoExpr.toString(event, b) + "'s visibility for " + playerExpr.toString(event, b);
	}
}
