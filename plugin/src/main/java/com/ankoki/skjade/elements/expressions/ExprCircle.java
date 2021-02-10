package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Circle")
@Description("Returns the points of the outline of a circle which ")
@Examples("set (circle at player's location with a radius of 10 and density 100) to red wool")
@Since("1.0.0")
public class ExprCircle extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprCircle.class, Location.class, ExpressionType.SIMPLE,
                "[a] circle (at|from) %location% with [a] radius [of] %number%(,| and) [with] [a] density [of] %number%");
    }

    private Expression<Location> center;
    private Expression<Number> radius;
    private Expression<Number> density;

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location c = center.getSingle(event);
        double r = radius.getSingle(event).doubleValue();
        double d = density.getSingle(event).doubleValue();
        if (c == null) return null;
        return getCircle(c, r, 1 / d).toArray(new Location[0]);
    }


    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "circle with center " + center.toString(event, b) + " with radius " + radius.toString(event, b) +
                " with density " + density.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        center = (Expression<Location>) exprs[0];
        radius = (Expression<Number>) exprs[1];
        density = (Expression<Number>) exprs[2];
        return true;
    }

    private List<Location> getCircle(Location centre, double radius, double density) {
        World world = centre.getWorld();
        double increment = (2 * Math.PI)/density;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < density; i++) {
            double angle = i * increment;
            double x = centre.getX() + (radius * Math.cos(angle));
            double z = centre.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, centre.getY(), z));
        }
        return locations;
    }
}
