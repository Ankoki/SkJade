package com.ankoki.skjade.hooks.elementals.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.events.EntitySpellCastEvent;
import com.ankoki.elementals.events.GenericSpellCastEvent;
import com.ankoki.elementals.events.SpellCastEvent;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Spell")
@Description("An elemental spell")
@Examples("set {_spell} to the elementals spell \"regrowth\"")
@RequiredPlugins("Elementals")
@Since("1.0")
public class ExprSpell extends SimpleExpression<Spell> {

    static {
        Skript.registerExpression(ExprSpell.class, Spell.class, ExpressionType.PROPERTY,
                "[the] [elementals] spell [(named|with [the] name)] %string%",
                "event[(-| )]spell");
    }

    private Expression<String> spell;
    private boolean inEvent;

    @Nullable
    @Override
    protected Spell[] get(Event event) {
        if (inEvent) {
            return new Spell[]{this.fromEvent(event)};
        }
        return new Spell[]{Utils.getSpellFromName(spell.getSingle(event))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Spell> getReturnType() {
        return Spell.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "spell";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        if (parseResult.mark == 1) {
            if (!ScriptLoader.isCurrentEvent(EntitySpellCastEvent.class) &&
                    ScriptLoader.isCurrentEvent(GenericSpellCastEvent.class) &&
                        ScriptLoader.isCurrentEvent(SpellCastEvent.class)) {
                Skript.error("You cannot use \"event-spell\" outside of a spell casting event!");
                return false;
            }
            inEvent = true;
            return true;
        }
        spell = (Expression<String>) exprs[0];
        return true;
    }

    private Spell fromEvent(Event e) {
        if (e instanceof EntitySpellCastEvent) {
            return ((EntitySpellCastEvent) e).getSpell();
        } else if (e instanceof GenericSpellCastEvent) {
            return ((GenericSpellCastEvent) e).getSpell();
        } else {
            return ((SpellCastEvent) e).getSpell();
        }
    }
}
