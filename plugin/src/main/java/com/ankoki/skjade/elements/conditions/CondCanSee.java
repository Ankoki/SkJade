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
@Since("1.0")
public class CondCanSee extends Condition {

    static {
        Skript.registerCondition(CondCanSee.class,
                "%entity% can see %entity%",
                "%entity% can([']t|not) see %entity%");
    }

    Expression<Entity> entity1, entity2;
    boolean can;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        can = i == 0;
        entity1 = (Expression<Entity>) exprs[0];
        entity2 = (Expression<Entity>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return entity1.toString(event, b) + " can see " + entity2.toString(event, b);
    }

    @Override
    public boolean check(Event event) {
        Entity eye = entity1.getSingle(event);
        Entity ent = entity2.getSingle(event);
        if (eye == null || ent == null) return false;
        if (!(eye instanceof LivingEntity) || !(ent instanceof LivingEntity)) return false;
        if (can) {
            return ((LivingEntity) eye).hasLineOfSight(ent);
        }
        return !((LivingEntity) eye).hasLineOfSight(ent);
    }
}
