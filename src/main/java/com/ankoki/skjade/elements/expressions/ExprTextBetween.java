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
import com.ankoki.roku.misc.StringUtils;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Text Between")
@Description("Gets the text between two characters.")
@Examples("set {_money} to the text between '[' and ']' from line 1 of event-item's lore")
@Since("1.1.0")
public class ExprTextBetween extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprTextBetween.class, String.class, ExpressionType.SIMPLE,
                "[the] text between %string% and %string% (from|in) %string%");
    }

    private Expression<String> firstExpr;
    private Expression<String> secondExpr;
    private Expression<String> stringExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        firstExpr = (Expression<String>) exprs[0];
        secondExpr = (Expression<String>) exprs[1];
        stringExpr = (Expression<String>) exprs[2];
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event event) {
        String first = firstExpr.getSingle(event);
        String second = secondExpr.getSingle(event);
        String string = stringExpr.getSingle(event);
        if (first == null || second == null || string == null) return new String[0];
        first = StringUtils.escape(first);
        second = StringUtils.escape(second);
        String[] split1 = string.split(first);
        if (split1.length < 2) return new String[0];
        String[] split2 = split1[split1.length - 1].split(String.valueOf(second));
        return new String[]{split2[0]};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "text between " + firstExpr.toString(e, debug) + " and " + secondExpr.toString(e, debug) + " from " + stringExpr.toString(e, debug);
    }

}
