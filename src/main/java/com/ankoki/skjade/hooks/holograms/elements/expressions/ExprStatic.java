package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Static State")
@Description("Gets/sets a holograms static state.")
@Examples("set {_static} to static state of hologram keyed as \"blackpink in your area\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class ExprStatic extends SimplePropertyExpression<SKJHolo, Boolean> {

	static {
		HoloProvider provider = HoloHandler.get().getCurrentProvider();
		if (provider.supportsStatic()) {
			register(ExprStatic.class, Boolean.class, "static state", "skjholo");
		}
	}

	@Override
	public @Nullable Boolean convert(SKJHolo skjHolo) {
		return skjHolo.isStatic();
	}

	@Override
	protected String getPropertyName() {
		return "static state";
	}

	@Override
	public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Boolean.class) : null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET && delta.length < 1) return;
		if (delta[0] instanceof Boolean bool) {
			SKJHolo holo = this.getExpr().getSingle(e);
			if (holo == null) return;
			holo.setStatic(bool);
		}
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}
}
