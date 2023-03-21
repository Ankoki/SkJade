package com.ankoki.skjade.hooks.holograms.elements.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

// Redundant as there is no way to differentiate between two event expressions with the same type.
public class ExprLineIndex extends EventValueExpression<Number> {

	/*static {
		Skript.registerExpression(ExprLineIndex.class, Number.class, ExpressionType.SIMPLE,
				"[event(-| )]line[(-| )index]");
	}*/

	public ExprLineIndex() {
		super(Number.class);
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "event-line-index";
	}

}
