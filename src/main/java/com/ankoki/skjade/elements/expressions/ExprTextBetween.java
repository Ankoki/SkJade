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

@Name("Text Between")
@Description("Gets the text between two characters.")
@Examples("set {_money} to the text between '[' and ']' from line 1 of event-item's lore")
@Since("1.1.0")
public class ExprTextBetween extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprTextBetween.class, String.class, ExpressionType.SIMPLE,
                "[the] text between %character% and %character% (from|in) %string%");
    }

    private Expression<Character> between1;
    private Expression<Character> between2;
    private Expression<String> string;

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (between1 == null || between2 == null || string == null) return new String[0];
        String first = String.valueOf(between1.getSingle(e));
        String second = String.valueOf(between2.getSingle(e));
        String str = string.getSingle(e);
        if (str == null) return new String[0];

        first = illegal(first);
        second = illegal(second);

        String[] split1 = str.split(first);
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
        return "text between " + between1.toString(e, debug) + " and " + between2.toString(e, debug) + " from " + string.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        between1 = (Expression<Character>) exprs[0];
        between2 = (Expression<Character>) exprs[1];
        string = (Expression<String>) exprs[2];
        return true;
    }

    private String illegal(String s) {
        switch (s) {
            case "[":
                s = "\\[";
                break;
            case "(":
                s = "\\(";
                break;
            case "{":
                s = "\\{";
                break;
            case "|":
                s = "\\|";
                break;
            case ".":
                s = "\\.";
                break;
            case "^":
                s = "\\^";
                break;
            case "$":
                s = "\\$";
                break;
            case "*":
                s = "\\*";
                break;
            case "+":
                s = "\\+";
                break;
            case "\\":
                s = "\\\\";
        }
        return s;
    }
}
