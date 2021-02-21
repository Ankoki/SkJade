package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

//needs testing, could break one line condition checks
public class ExprBoolean extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprBoolean.class, Boolean.class, ExpressionType.SIMPLE,
                "<.+>");
    }

    private Condition condition;

    @Nullable
    @Override
    protected Boolean[] get(Event e) {
        return new Boolean[]{condition.check(e)};
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
    public String toString(@Nullable Event e, boolean debug) {
        return condition.toString();
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        String unparsed = parseResult.regexes.get(1).group(0);
        condition = Condition.parse(unparsed, "Can't understand this condition: " + unparsed);
        return true;
    }
}
