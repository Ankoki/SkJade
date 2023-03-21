package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Midnight")
@Description("Returns the date of midnight.")
@Examples("set {_midnight} to midnight")
@Since("1.0.0")
public class ExprMidnight extends SimpleExpression<Date> {

    static {
        Skript.registerExpression(ExprMidnight.class, Date.class, ExpressionType.SIMPLE,
                "midnight");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected Date[] get(Event event) {
        long mid = ((long) (System.currentTimeMillis() / 8.64e7) + 1) * 86400000;
        return new Date[]{new Date(mid)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "midnight";
    }

}
