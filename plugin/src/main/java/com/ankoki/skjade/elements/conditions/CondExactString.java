package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Exact String")
@Description("Checks if a string is exactly equal to another string.")
@Examples("add 1 to {%player's uuid%::champ} if {%player's uuid%::key} = \"afgaIgHaWU29oNE\"")
@Since("1.0.0")
public class CondExactString extends Condition {

    static {
        Skript.registerCondition(CondExactString.class,
                "%string% [(1¦(does(n't | not )|!))](exactly equal[s]|==) %string%",
                "%string% (is|1¦is(n't| not)) exactly equal to %string%");
    }

    Expression<String> string1;
    Expression<String> string2;

    @Override
    public boolean check(Event event) {
        String str1 = string1.getSingle(event);
        String str2 = string2.getSingle(event);
        if (str1 == null || str2 == null) return false;
        return isNegated() == str1.equals(str2);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "string is identical to string";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        setNegated(parseResult.mark == 1);
        string1 = (Expression<String>) exprs[0];
        string2 = (Expression<String>) exprs[1];
        return true;
    }
}
