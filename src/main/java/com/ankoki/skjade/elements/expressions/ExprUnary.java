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
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Unary Value")
@Description("Returns the unary value of a number.")
@Examples("broadcast \"%-{hello::%player's uuid%}\"")
@Since("1.3.0")
public class ExprUnary extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprUnary.class, Number.class, ExpressionType.SIMPLE,
                "(-|unary [value of ])%~number%");
    }

    private Expression<Number> numberExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        numberExpr = (Expression<Number>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event event) {
        Number number = numberExpr.getSingle(event);
        if (number == null) return new Number[0];
        return new Number[]{number.doubleValue() * -1};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "unary value of " + numberExpr.toString(e, debug);
    }

}
