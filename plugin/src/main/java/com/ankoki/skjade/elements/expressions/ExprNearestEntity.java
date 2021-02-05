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
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.TreeMap;

@Name("Nearest Entity")
@Description("Returns the nearest entity to a location.")
@Examples("nearest entity to player's location")
@Since("1.0.0")
public class ExprNearestEntity extends SimpleExpression<Entity> {

    static {
        Skript.registerExpression(ExprNearestEntity.class, Entity.class, ExpressionType.SIMPLE,
                "[the] (nearest|closest) entity to %location%");
    }

    private Expression<Location> location;

    @Nullable
    @Override
    protected Entity[] get(Event e) {
        Location loc = location.getSingle(e);
        if (loc == null) return null;
        return new Entity[]{getClosestEntity(loc)};
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
        return "closest entity to " + location.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        return true;
    }

    private Entity getClosestEntity(Location location) {
        TreeMap<Double, Entity> map = new TreeMap<>();
        for (Entity ent : location.getWorld().getEntities()) {
            map.put(location.distanceSquared(ent.getLocation()), ent);
        }
        return map.firstEntry().getValue();
    }
}
