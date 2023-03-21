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

@Name("Multiple Conditions")
@Description("Allows you to use multiple conditions.")
@Examples("if attacker is online && victim is online:")
@Since("1.0.0")
public class CondMultiple extends Condition {

    static {
        Skript.registerCondition(CondMultiple.class,
                "<.+> && <.+>",
                "<.+> \\|\\| <.+>");
    }

    private Condition first;
    private Condition second;
    private boolean isAnd;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        String unparsed1 = parseResult.regexes.get(0).group(0);
        String unparsed2 = parseResult.regexes.get(1).group(0);
        first = Condition.parse(unparsed1, "Can't understand this condition: " + unparsed1);
        second = Condition.parse(unparsed2, "Can't understand this condition: " + unparsed2);
        isAnd = i == 0;
        return first != null && second != null;
    }

    @Override
    public boolean check(Event event) {
        boolean b1 = first.check(event);
        boolean b2 = second.check(event);
        return isAnd ? b1 && b2 : b1 || b2;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return first.toString() + (isAnd ? " && " : " || ") + second.toString();
    }

}
