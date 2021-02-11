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
import org.bukkit.Bukkit;
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
                "[the] (nearest|closest) entity to (%location%|1¦%entity%)");
    }

    private Expression<Location> location;
    private Expression<Entity> entity;

    @Nullable
    @Override
    protected Entity[] get(Event e) {
        Location loc;
        Entity ent = null;
        if (location == null) {
            ent = entity.getSingle(e);
            if (ent == null) return null;
            if (!(ent instanceof LivingEntity)) return null;
            loc = ent.getLocation();
        } else loc = location.getSingle(e);
        if (loc == null) return null;

        Entity result = null;
        double lastDistance = Double.MAX_VALUE;
        if (ent == null) Bukkit.broadcastMessage("entity null wot");
        for (Entity allEnts : loc.getWorld().getEntities()) {
            Bukkit.broadcastMessage("allEnt name: " + allEnts.getName());
            if (ent != null) {
                Bukkit.broadcastMessage("ents name: " + ent.getName());
            }
            if (ent != null && allEnts == ent) continue;
            double dist = loc.distanceSquared(allEnts.getLocation());
            if (dist < lastDistance) {
                result = allEnts;
                lastDistance = dist;
            }
        }
        if (result == null) {
            Bukkit.broadcastMessage("result null huh");
        }
        else Bukkit.broadcastMessage("result name: " + result.getName());
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
        return "closest entity to " + (location == null ? entity.toString(e, debug) : location.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (parseResult.mark != 1) location = (Expression<Location>) exprs[0];
        else entity = (Expression<Entity>) exprs[0];
        return true;
    }
}
