package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

public class ExprHologramLocation extends SimplePropertyExpression<Hologram, Location> {

	static {
		register(ExprHologramLocation.class, Location.class, "location", "holograms");
	}

	@Override
	protected String getPropertyName() {
		return "location";
	}

	@Override
	public @Nullable Location convert(Hologram hologram) {
		return hologram.getLocation();
	}

	@Override
	public @Nullable
	Class<?>[] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Location.class) : new Class<?>[0];
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		assert mode == ChangeMode.SET;
		if (delta[0] instanceof Location) {
			Location location = ((Location) delta[0]);
			Arrays.stream(this.getExpr().getArray(e)).forEach(holo -> holo.teleport(location));
		}
	}

	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

}
