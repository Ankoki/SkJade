package com.ankoki.skjade.hooks.elementals.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Spell's ID")
@Description("An elemental spells ID.")
@Examples("if spell \"medic\"'s id > 300:")
@RequiredPlugins("Elementals")
@Since("1.0")
public class ExprSpellID extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprSpellID.class, Number.class, ExpressionType.SIMPLE,
                "[the] id of %spell%",
                "%spell%'s id");
    }

    private Expression<Spell> spell;

    @Nullable
    @Override
    protected Number[] get(Event event) {
        if (spell.getSingle(event) == null) return new Number[]{0};
        return new Number[]{spell.getSingle(event).getId()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "id of spell";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        spell = (Expression<Spell>) exprs[0];
        return true;
    }
}
