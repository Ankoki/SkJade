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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//This already exists but the fucks i give are through the floor
@Name("Current Date")
@Description("Gets the current date and formats as the date either as " +
             "\"HH:mm:ss dd/MM/yyyy\" or as the one specified, must follow these guidelines: " +
             "https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html")
@Examples("send \"%the current date formatted as \"\"HH:mm:ss dd/MM/yyyy\"\"%\"")
@Since("1.0.0")
public class ExprCurrentDate extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprCurrentDate.class, String.class, ExpressionType.PROPERTY,
                "[the] current date [formatted as %-string%]");
    }

    private Expression<String> dateFormat;

    @Nullable
    @Override
    protected String[] get(Event event) {
        DateFormat dF;
        if (dateFormat == null) {
            dF = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        } else {
            dF = new SimpleDateFormat(dateFormat.getSingle(event));
        }
        return new String[]{dF.format(new Date())};
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
    public String toString(@Nullable Event event, boolean b) {
        return "current date";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        dateFormat = (Expression<String>) exprs[0];
        return true;
    }
}
