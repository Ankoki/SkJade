package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Persistent State")
@Description("Sets/gets whether a hologram is/will be persistent upon restart. " +
		"The hologram id given will be used for saving it in the plugins configuration, " +
		"so make sure to be wary of any clashes.")
@Examples("broadcast {_holo}'s persistent state")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprPersistent extends SimplePropertyExpression<SKJHolo, Boolean> {

	static {
		register(ExprPersistent.class, Boolean.class, "persistent stat(us|e)", "skjholo");
	}

	@Override
	protected String getPropertyName() {
		return "persistent state";
	}

	@Override
	public @Nullable Boolean convert(SKJHolo skjHolo) {
		return skjHolo.isPersistent();
	}

	@Override
	public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET) return CollectionUtils.array(Boolean.class);
		return null;
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET ||
				delta.length == 0 ||
				!(delta[0] instanceof Boolean bool)) return;
		SKJHolo holo = this.getExpr().getSingle(event);
		if (holo == null)
			return;
		holo.setPersistent(bool);
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}
}
