package com.ankoki.skjade.hooks.holograms.elements.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprHologram extends EventValueExpression<SKJHolo> {

	static {
		Skript.registerExpression(ExprHologram.class, SKJHolo.class, ExpressionType.SIMPLE,
				"[event(-| )]holo[gram]");
	}

	public ExprHologram() {
		super(SKJHolo.class);
	}

	@Override
	public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.DELETE ? CollectionUtils.array() : null;
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (mode != ChangeMode.DELETE)
			return;
		SKJHolo holo = this.getSingle(event);
		if (holo == null)
			return;
		holo.destroy();
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "event-hologram";
	}
}
