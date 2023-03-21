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
@Description("Gets the version  of the server. You can get the minor (x.>X<.x) or the patch (x.x.>X<) version.")
@Examples("if the server minor version > 16:")
@Since("2.0")
public class ExprServerVersion extends SimpleExpression<Number> {

	static {
		Skript.registerExpression(ExprServerVersion.class, Number.class, ExpressionType.SIMPLE,
				"[the] [current] server :[minor|patch] version");
	}

	private boolean minor;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		minor = parseResult.hasTag("minor");
		return true;
	}

	@Override
	protected Number[] get(Event event) {
		return new Number[]{minor ? Utils.getMinecraftMinor() : Utils.getMinecraftPatch()};
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
