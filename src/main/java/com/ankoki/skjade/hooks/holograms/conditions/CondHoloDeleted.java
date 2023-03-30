package com.ankoki.skjade.hooks.holograms.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class CondHoloDeleted extends Condition {

	static {
		Skript.registerCondition(CondHoloDeleted.class,
				"%hologram% (has been|is) deleted");
	}

	private Expression<Hologram> holoExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		holoExpr = (Expression<Hologram>) exprs[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		Hologram holo = holoExpr.getSingle(event);
		if (holo == null)
			return true;
		return holo.isDeleted();
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return holoExpr.toString(event, b) + " has been deleted";
	}

}
