package com.ankoki.skjade.hooks.elementals.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import com.ankoki.elementals.managers.Spell;
import jdk.jfr.Description;
import jdk.jfr.Name;

@Name("Spell is Prolonged")
@Description("Checks if a spell is a prolonged spell or not.")
@Examples("if event-spell is prolonged:")
@Since("1.0.0")
public class CondIsProlonged extends PropertyCondition<Spell> {

    static {
        register(CondIsProlonged.class, "[elementals] prolonged [spell]", "spells");
    }

    @Override
    public boolean check(Spell spell) {
        return spell.isProlonged();
    }

    @Override
    protected String getPropertyName() {
        return "prolonged";
    }
}
