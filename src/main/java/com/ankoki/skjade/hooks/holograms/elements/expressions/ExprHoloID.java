package com.ankoki.skjade.hooks.holograms.elements.expressions;


import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram's ID")
@Description("Gets a holograms key/id.")
@Examples("if {_holo}'s key = \"born pink\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprHoloID extends SimplePropertyExpression<SKJHolo, String> {

	static {
		register(ExprHoloID.class, String.class, "[holo[(gram|phic)]] (id|key)", "skjholo");
	}

	@Override
	protected String getPropertyName() {
		return "holographic key";
	}

	@Override
	public @Nullable String convert(SKJHolo skjHolo) {
		return skjHolo.getKey();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
