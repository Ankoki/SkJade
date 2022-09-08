package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.HoloManager;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Static State")
@Description("Gets/sets a holograms static state.")
@Examples("set {_static} to static state of hologram keyed as \"blackpink in your area\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class ExprStatic extends SimpleExpression<Boolean> {

	static {
		HoloProvider provider = HoloManager.get().getCurrentProvider();
		if (provider.supportsStatic()) {
			Skript.registerExpression(ExprStatic.class, Boolean.class, ExpressionType.SIMPLE,
					"static state of %skjholo%",
					"%skjholo%'s static state");
		} else SkJade.getInstance().getLogger().warning("Please note your current hologram provider (" + provider.getId() + ") " +
				"does not support the use of static holograms! You cannot use the static expression.");
	}

	private Expression<SKJHolo> holoExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		holoExpr = (Expression<SKJHolo>) exprs[0];
		return true;
	}

	@Override
	protected @Nullable Boolean[] get(Event event) {
		SKJHolo holo = holoExpr.getSingle(event);
		if (holo == null) return new Boolean[0];
		return new Boolean[]{holo.isStatic()};
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
	public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Boolean.class) : null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET && delta.length < 1) return;
		if (delta[0] instanceof Boolean bool) {
			SKJHolo holo = holoExpr.getSingle(e);
			if (holo == null) return;
			holo.setStatic(bool);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return holoExpr.toString(event, b) + "'s static state";
	}
}
