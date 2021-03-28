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

@Name("Formatted Number")
@Description("Formats a number over 999 to be in readable form, such as 2000 -> 2,000")
@Examples("broadcast \"%player%'s Balance: $%formatted number {eco::%%player's uuid%%}%")
@Since("1.2.0")
public class ExprFormatNumber extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprFormatNumber.class, String.class, ExpressionType.SIMPLE,
                "[the] format[ted [number]] %number%");
    }

    private Expression<Number> number;

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (number == null) return null;
        double num = number.getSingle(e).doubleValue();
        String n = "" + num;
        String[] split = n.split("(?<=\\d(?=(\\d{3}) (?!\\d)))");
        StringBuilder builder = new StringBuilder();
        for (String s : split) {
            builder.append(s).append(",");
        }
        return new String[]{builder.toString()};
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
        return "the formatted number " + number.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        number = (Expression<Number>) exprs[0];
        return true;
    }
}
