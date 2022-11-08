package com.ankoki.skjade.hooks.holograms.elements.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprPageIndex extends EventValueExpression<Number> {

	static {
		Skript.registerExpression(ExprPageIndex.class, Number.class, ExpressionType.SIMPLE,
				"[event(-| )][page(-| )]index");
	}

	public ExprPageIndex() {
		super(Number.class);
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "event-page-index";
	}
}
