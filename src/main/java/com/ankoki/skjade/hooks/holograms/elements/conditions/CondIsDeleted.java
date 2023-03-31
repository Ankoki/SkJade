package com.ankoki.skjade.hooks.holograms.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Is Hologram Deleted")
@Description("Checks if a hologram has been deleted or not.")
@Examples("if {-pets::%player's uuid%::holo} has been deleted:")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class CondIsDeleted extends Condition {

	static {
		Skript.registerCondition(CondIsDeleted.class,
				"%skjholo% (is|has been) deleted",
				"%skjholo% (isn[']t|hasn[']t been) deleted");
	}

	private Expression<SKJHolo> holoExpr;
	private boolean negated;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		negated = i == 1;
		holoExpr = (Expression<SKJHolo>) exprs[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		SKJHolo holo = holoExpr.getSingle(event);
		if (holo == null)
			return true;
		if (negated)
			return !holo.isDeleted();
		else
			return holo.isDeleted();
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return holoExpr.toString(event, debug) + (negated ? " hasn't been " : " has been ") + "deleted";
	}

}
