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
import org.jetbrains.annotations.Nullable;

@Name("Text Between")
@Description("Gets the text between two characters.")
@Examples("set {_money} to the text between \"[]\" from line 1 of event-item's lore")
@Since("insert version")
public class ExprTextBetween extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprTextBetween.class, String.class, ExpressionType.SIMPLE,
                "[the] text between %string% from %string%");
    }

    private Expression<String> between;
    private Expression<String> string;

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (between == null || string == null) return null;
        String btwn = between.getSingle(e);
        String str = string.getSingle(e);
        if (btwn == null || str == null) return null;
        String[] split = btwn.split("");
        if (split.length != 2) return null;
        String[] spltStr = str.split(split[0]);
        if (spltStr.length <= 1) return null;
        return new String[]{spltStr[1].split(split[1])[0]};
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
        return "text between " + between.toString(e, debug) + " from " + string.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        between = (Expression<String>) exprs[0];
        string = (Expression<String>) exprs[1];
        return true;
    }
}
