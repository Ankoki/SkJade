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
import org.jetbrains.annotations.Nullable;

@Name("Boolean")
@Description("Shortens statements such as true = true to just true.")
@Examples("if true:")
@Since("1.0.0")
public class CondBoolean extends Condition {

    static {
        Skript.registerCondition(CondBoolean.class,
                "%booleans%");
    }

    private Expression<Boolean> bool;

    @Override
    public boolean check(Event e) {
        if (bool == null) return false;
        Boolean b = bool.getSingle(e);
        if (b == null) return false;
        return b;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return bool.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        bool = (Expression<Boolean>) exprs[0];
        return true;
    }
}
