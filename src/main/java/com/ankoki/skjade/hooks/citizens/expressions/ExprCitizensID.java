package com.ankoki.skjade.hooks.citizens.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprCitizensID extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprCitizensID.class, Number.class, ExpressionType.PROPERTY,
                "[the] id [number] of [[the] (citizen|npc)] %entity%");
    }

    private Expression<Entity> entity;

    @Nullable
    @Override
    protected Number[] get(Event event) {
        return new Number[]{CitizensAPI.getNPCRegistry().getNPC(entity.getSingle(event)).getId()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "npc id";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        entity = (Expression<Entity>) exprs[0];
        return true;
    }
}
