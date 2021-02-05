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
import org.bukkit.entity.Player;
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
                "[the] (nearest|closest) entity to (%location%|1¦%player%)");
    }

    private Expression<Location> location;
    private Expression<Player> player;

    @Nullable
    @Override
    protected Entity[] get(Event e) {
        Location loc;
        Player p = null;
        if (location == null) {
            p = player.getSingle(e);
            if (p == null) return null;
            loc = p.getLocation();
        }
        else loc = location.getSingle(e);
        if (loc == null) return null;
        return new Entity[]{p == null ? getClosestEntity(loc, false) : getClosestEntity(loc, true, p)};
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
        return "closest entity to " + (location == null ? player.toString(e, debug) : location.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (parseResult.mark != 1) location = (Expression<Location>) exprs[0];
        else player = (Expression<Player>) exprs[0];
        return true;
    }

    private Entity getClosestEntity(Location location, boolean ignorePlayer, Player... player) {
        TreeMap<Double, Entity> map = new TreeMap<>();
        for (Entity ent : location.getWorld().getEntities()) {
            if (ignorePlayer) {
                if (ent != player[0]) {
                    map.put(location.distanceSquared(ent.getLocation()), ent);
                }
            } else {
                map.put(location.distanceSquared(ent.getLocation()), ent);
            }
        }
        return map.firstEntry().getValue();
    }
}
