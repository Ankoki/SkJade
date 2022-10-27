package com.ankoki.skjade.hooks.holograms.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.elements.effects.sub.EffHoloPage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Show Hologram Page")
@Description("Shows a hologram page or its default lines to a player. Some hologram providers do not " +
		"support pages though, so this syntax may not be enabled.")
@Examples("show page 2 of {_holo} to all players")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class EffShowPage extends Effect {

	static {
		HoloProvider provider = HoloHandler.get().getCurrentProvider();
		if (provider.supportsPages())
			Skript.registerEffect(EffHoloPage.class,
					"show (page %-number%|[default ]lines) of %skjholo% to %players%");
	}

	private Expression<Number> numberExpr;
	private Expression<SKJHolo> holoExpr;
	private Expression<Player> playerExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		numberExpr = (Expression<Number>) exprs[0];
		holoExpr = (Expression<SKJHolo>) exprs[1];
		playerExpr = (Expression<Player>) exprs[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Number number = numberExpr.getSingle(event);
		int page = number != null ? number.intValue() : 0;
		SKJHolo holo = holoExpr.getSingle(event);
		Player[] players = playerExpr.getAll(event);
		if (holo == null)
			return;
		holo.showTo(page, players);
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "show page " + (numberExpr == null ? "0" : numberExpr.toString(event, b)) +
				" of " + holoExpr.toString(event, b) + " to " + playerExpr.toString(event, b);
	}
}
