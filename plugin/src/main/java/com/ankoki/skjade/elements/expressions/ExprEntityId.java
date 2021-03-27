package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("ID of Entity")
@Description("Returns the id of an entity.")
@Examples("broadcast \"%event-entity's id%\"")
@Since("1.2.0")
public class ExprEntityId extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprEntityId.class, Number.class, ExpressionType.SIMPLE,
                "%entity%'s id",
                "the id of %entity%");
    }

    private Expression<Entity> entity;

    @Nullable
    @Override
    protected Number[] get(Event e) {
        if (entity == null) return null;
        Entity ent = entity.getSingle(e);
        if (ent == null) return null;
        return new Number[]{ent.getEntityId()};
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
    public String toString(@Nullable Event e, boolean debug) {
        return entity.toString(e, debug) + "'s id";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entity = (Expression<Entity>) exprs[0];
        return true;
    }
}
