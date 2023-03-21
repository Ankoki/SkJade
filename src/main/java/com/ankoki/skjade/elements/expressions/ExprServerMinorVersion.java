package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Server Minor Version")
@Description("Gets the server minor version (x.>X<.x) of the server.")
@Examples("if the server version > 16:")
@Since("2.0")
public class ExprServerMinorVersion extends SimpleExpression<Number> {

	static {
		Skript.registerExpression(ExprServerMinorVersion.class, Number.class, ExpressionType.SIMPLE,
				"[the] [current] server [minor] version");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		return true;
	}

	@Override
	protected Number[] get(Event event) {
		return new Number[]{Utils.getMinecraftMinor()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Integer.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the server version";
	}

}
