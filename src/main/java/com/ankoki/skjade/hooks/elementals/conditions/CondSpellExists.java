package com.ankoki.skjade.hooks.elementals.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Spell Exists")
@Description("Checks if an elementals spell exists.")
@Examples("if the spell \"jays spell\" exists:")
@RequiredPlugins("Elementals")
@Since("1.0.0")
public class CondSpellExists extends Condition {

    static {
        Skript.registerCondition(CondSpellExists.class,
                "%spell% (is a valid spell|exists)");
    }

    private Expression<Spell> spell;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        spell = (Expression<Spell>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (spell == null) return false;
        Spell s = spell.getSingle(event);
        return s != null;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "spell exists";
    }
}
