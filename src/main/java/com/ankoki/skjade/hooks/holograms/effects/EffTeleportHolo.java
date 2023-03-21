package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

public class EffTeleportHolo extends Effect {

	static {
		Skript.registerEffect(EffTeleportHolo.class,
				"teleport holo[gram][s] %holograms% to %location%");
	}

	private Expression<Hologram> hologramExpr;
	private Expression<Location> locationExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		hologramExpr = (Expression<Hologram>) exprs[0];
		locationExpr = (Expression<Location>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Hologram[] holograms = hologramExpr.getArray(event);
		Location location = locationExpr.getSingle(event);
		if (location == null)
			return;
		Arrays.stream(holograms).forEach(holo -> holo.teleport(location));
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "teleport holo " + hologramExpr.toString(event, debug) + " to " + locationExpr.toString(event, debug);
	}

}
