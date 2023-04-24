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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Timespan in Ticks")
@Description("Converts a timespan into ticks.")
@Examples("broadcast \"%20 seconds in ticks%\"")
@Since("1.3.0")
public class ExprTimespanTicks extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprTimespanTicks.class, Number.class, ExpressionType.PROPERTY,
                "%timespan% in ticks",
                "ticks of %timespan%",
                "[the] amount of ticks in %timespan%");
    }

    private Expression<Timespan> timespanExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        timespanExpr = (Expression<Timespan>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event event) {
        Timespan timespan = timespanExpr.getSingle(event);
        if (timespan == null) return new Number[0];
        return new Number[]{timespan.getTicks_i()};
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
        return timespanExpr.toString(e, debug) + " in ticks";
    }

}
