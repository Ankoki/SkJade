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
import org.eclipse.jdt.annotation.Nullable;

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

    private Expression<Entity> entityExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entityExpr = (Expression<Entity>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event event) {
        Entity entity = entityExpr.getSingle(event);
        if (entity == null) return new Number[0];
        return new Number[]{entity.getEntityId()};
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
        return entityExpr.toString(e, debug) + "'s id";
    }
}
