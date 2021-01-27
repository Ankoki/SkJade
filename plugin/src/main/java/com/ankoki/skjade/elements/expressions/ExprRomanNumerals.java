package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Roman Numerals")
@Description("Returns the given number in roman numerals.")
@Examples("send \"Sharpness level: %roman numeral of (level of sharpness of player's tool)%\"")
@Since("1.0.0")
public class ExprRomanNumerals extends PropertyExpression<Integer, String> {

    static {
        Skript.registerExpression(ExprRomanNumerals.class, String.class, ExpressionType.PROPERTY,
                "roman numeral");
    }

    @Override
    protected String[] get(Event event, Integer[] integers) {
        int n = integers[0];
        String[] roman = "M|CM|D|CD|C|XC|L|XL|X|IX|V|IV|I".split("\\|");
        String[] numbers = "1000|900|500|400|100|90|50|40|10|9|5|4|1".split("\\|");
        int i = 0;
        String r = "";
        for (String s : numbers) {
            i++;
            int next = 0;
            int parsed = Integer.parseInt(s);
            while (n >= parsed) {
                next++;
                n -= parsed;
                r += roman[i--];
            }
        }
        return new String[]{r.isEmpty() ? "" : r};
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "roman numeral";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        return true;
    }
}
