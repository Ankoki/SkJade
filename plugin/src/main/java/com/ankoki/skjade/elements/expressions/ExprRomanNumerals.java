package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.utils.Utils;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> allRomans = new ArrayList<>();
        Arrays.stream(integers).forEach(i -> {
            allRomans.add(Utils.toRoman(i));
        });
        return (String[]) allRomans.toArray();
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
