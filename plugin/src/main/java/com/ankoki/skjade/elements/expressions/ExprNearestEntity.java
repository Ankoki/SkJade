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
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.TreeMap;

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

        TreeMap<Double, Entity> map = new TreeMap<>();
        for (Entity allEnts : loc.getWorld().getEntities()) {
            if (ent != null && allEnts == ent) continue;
            map.put(loc.distanceSquared(allEnts.getLocation()), ent);
        }

        return new Entity[]{map.firstEntry().getValue()};
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
