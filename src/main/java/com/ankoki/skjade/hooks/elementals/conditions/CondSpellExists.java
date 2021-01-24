package com.ankoki.skjade.hooks.elementals.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.api.ElementalsAPI;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class CondSpellExists extends Condition {

    static {
        Skript.registerCondition(CondSpellExists.class,
                "%spell% (is a valid spell|exists)");
    }

    private Expression<String> spell;

    @Override
    public boolean check(Event event) {
        return ElementalsAPI.valueOf(spell.getSingle(event)) != null;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "spell exists";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        spell = (Expression<String>) exprs[0];
        return true;
    }
}
