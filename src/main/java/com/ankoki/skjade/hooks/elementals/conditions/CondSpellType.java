package com.ankoki.skjade.hooks.elementals.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.api.EntitySpell;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.skjade.utils.Utils.SpellType;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Spell Type")
@Description("Checks if the spell is either generic, entity, prolonged generic, or prolonged entity.")
@Examples("if the spell \"umbrial\" is an entity spell:")
@RequiredPlugins("Elementals")
@Since("1.0")
public class CondSpellType extends Condition {

    static {
        Skript.registerCondition(CondSpellType.class,
                "%spell% is [a[n]] (0¦generic|1¦entity|2¦prolonged generic|3¦prolonged entity) [spell]");
    }

    private SpellType spellType;
    private Expression<Spell> spellExprs;

    @Override
    public boolean check(Event event) {
        Spell spell = spellExprs.getSingle(event);
        if (spell == null) return false;
        switch (spellType) {
            case GENERIC:
                return (spell instanceof GenericSpell && !spell.isProlonged());
            case ENTITY:
                return (spell instanceof EntitySpell && !spell.isProlonged());
            case GENERIC_PROLONGED:
                return (spell instanceof GenericSpell && spell.isProlonged());
            case ENTITY_PROLONGED:
                return (spell instanceof EntitySpell && spell.isProlonged());
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "if current spell is generic, entity, or prolonged";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        spellExprs = (Expression<Spell>) exprs[0];
        switch (parseResult.mark) {
            case 0:
                spellType = SpellType.GENERIC;
                break;
            case 1:
                spellType = SpellType.ENTITY;
                break;
            case 2:
                spellType = SpellType.GENERIC_PROLONGED;
                break;
            case 3:
                spellType = SpellType.ENTITY_PROLONGED;
        }
        return true;
    }
}
