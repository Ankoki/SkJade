package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Line's Index")
@Description("Gets a hologram line's index.")
@Examples("if {_line}'s index > 4:")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprHoloLineIndex extends SimplePropertyExpression<SKJHoloLine, Number> {

	static {
		register(ExprHoloLineIndex.class, Number.class, "(index|number)", "skjhololine");
	}

	@Override
	protected String getPropertyName() {
		return "index";
	}

	@Override
	public @Nullable Number convert(SKJHoloLine line) {
		return line.getIndex();
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
