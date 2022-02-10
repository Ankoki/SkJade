package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Nearest Entity")
@Description("Returns the nearest entity to a location.")
@Examples("kill the closest entity to player")
@Since("1.0.0")
public class ExprNearestEntity extends SimpleExpression<Entity> {

    static {
        Skript.registerExpression(ExprNearestEntity.class, Entity.class, ExpressionType.SIMPLE,
                "[the] (nearest|closest) (1¦[entity of type] %-entitytype%|entity) to %entity%",
                "[the] (nearest|closest) entity (1¦[entity of type] %-entitytype%|entity) to %location%");
    }

    private Expression<Location> locationExpr;
    private Expression<Entity> entityExpr;
    private Expression<EntityType> entityTypeExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        boolean entityType = parseResult.mark == 1;
        if (matchedPattern == 1) {
            entityTypeExpr = entityType ? (Expression<EntityType>) exprs[0] : null;
            locationExpr = entityType ? (Expression<Location>) exprs[1] : (Expression<Location>) exprs[0];
            return true;
        }
        entityTypeExpr = entityType ? (Expression<EntityType>) exprs[0] : null;
        entityExpr = entityType ? (Expression<Entity>) exprs[1] : (Expression<Entity>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Entity[] get(Event e) {
        Location loc;
        Entity ent = null;
        EntityType type = null;
        if (entityTypeExpr != null) {
            type = entityTypeExpr.getSingle(e);
        }

        if (locationExpr == null) {
            ent = entityExpr.getSingle(e);
            if (ent == null) return new Entity[0];
            if (!(ent instanceof LivingEntity)) return new Entity[0];
            loc = ent.getLocation();
        } else loc = locationExpr.getSingle(e);
        if (loc == null) return new Entity[0];

        Entity result = null;
        double lastDistance = Double.MAX_VALUE;
        for (Entity allEnts : loc.getWorld().getEntities()) {
            if (ent != null && allEnts == ent) continue;
            double dist = loc.distanceSquared(allEnts.getLocation());
            if (dist < lastDistance) {
                if (type != null) {
                    if (type.isInstance(allEnts)) {
                        result = allEnts;
                        lastDistance = dist;
                    }
                } else {
                    result = allEnts;
                    lastDistance = dist;
                }
            }
        }
        return new Entity[]{result};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "closest entity to " + (locationExpr == null ? entityExpr.toString(e, debug) : locationExpr.toString(e, debug));
    }
}
