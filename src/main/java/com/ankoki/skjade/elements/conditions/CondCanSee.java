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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Can See")
@Description("Checks if a living entity can see another living entity.")
@Examples("if player can see loop-player:")
@Since("1.0.0")
public class CondCanSee extends Condition {

    static {
        Skript.registerCondition(CondCanSee.class,
                "%entity% can see %entity%",
                "%entity% can([']t|not) see %entity%");
    }

    Expression<Entity> firstExpr, secondExpr;
    boolean can;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        can = i == 0;
        firstExpr = (Expression<Entity>) exprs[0];
        secondExpr = (Expression<Entity>) exprs[1];
        return true;
    }

    @Override
    public boolean check(Event event) {
        Entity first = firstExpr.getSingle(event);
        Entity second = secondExpr.getSingle(event);
        if (first == null || second == null) return false;
        if (first instanceof LivingEntity living) return can == living.hasLineOfSight(second);
        else return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return firstExpr.toString(event, debug) + " can see " + secondExpr.toString(event, debug);
    }
}
