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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Show/Hide Hologram")
@Description("Shows or hides a hologram for a player. Some hologram providers do not support visibility or require " +
		"an external plugin (such as ProtocolLib), so this syntax may not be enabled. " +
		"If pages are supported and you want to show a certain page, use the Show Page syntax.")
@Examples("reveal {_holo} to all players where [input is flying]")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class EffShowHolo extends Effect {

	static {
		HoloProvider provider = HoloHandler.get().getCurrentProvider();
		if (provider.supportsPerPlayer())
			Skript.registerEffect(EffShowHolo.class,
					"(show|reveal) %skjholo% to %players%",
					"(hide|conceal) %skjholo% from %players%");
	}

	private Expression<SKJHolo> holoExpr;
	private Expression<Player> playerExpr;
	private boolean hide;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		holoExpr = (Expression<SKJHolo>) exprs[0];
		playerExpr = (Expression<Player>) exprs[1];
		hide = i == 1;
		return true;
	}

	@Override
	protected void execute(Event event) {
		SKJHolo holo = holoExpr.getSingle(event);
		Player[] players = playerExpr.getAll(event);
		if (holo == null)
			return;
		if (hide)
			holo.hideFrom(players);
		else
			holo.showTo(0, players);
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return (hide ? "hide " : "show ") + holoExpr.toString(event, b) + (hide ? " from " : " to ") +
				playerExpr.toString(event, b);
	}
}
