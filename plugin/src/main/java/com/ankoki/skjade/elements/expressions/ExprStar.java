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
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Star")
@Description("Returns the points of the outline of a star from the center, radius, and density")
@Examples("show happy villager at star at block 8 in front of player with 5 points, with radius 10 and density 5")
@Since("1.0.0")
public class ExprStar extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprStar.class, Location.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] [(loc[ation]s|points) of] [a] star (at|from) %location% with %integer% points, (with|of) [a] radius [of] %number%(,| and) [a] density [of] %number%");
    }

    private Expression<Location> center;
    private Expression<Integer> points;
    private Expression<Number> radius;
    private Expression<Number> density;

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location c = center.getSingle(event);
        int p = points.getSingle(event);
        double r = radius.getSingle(event).doubleValue();
        double d = density.getSingle(event).doubleValue();
        if (c == null) return null;
        return (Location[]) getStar(c, r, p, d).toArray();
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
        return "star with center " + center.toString(event, b) + " with radius " + radius.toString(event, b) +
                " with density " + density.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        center = (Expression<Location>) exprs[0];
        points = (Expression<Integer>) exprs[1];
        radius = (Expression<Number>) exprs[2];
        density = (Expression<Number>) exprs[3];
        return true;
    }

    private List<Location> getStar(Location origin, double radius, int vertices, double density) {
        List<Location> locations = new ArrayList<>();
        double points = ((vertices * (vertices + 1D) / 2D - (vertices * 2) * density));
        double delta = 360D/vertices;
        List<Location> vertex = new ArrayList<>();
        for (int i = 1; i <= vertices; i++) {
            vertex.add(origin.add(new Vector(Math.cos(delta * i), 0, Math.cos(delta * i)).multiply(radius)));
        }
        int index = 0;
        for (Location location : vertex) {
            index++;
            List<Integer> neighbors = new ArrayList<>();
            neighbors.add(index < vertices ? index++ : 1);
            neighbors.add(index > 1 ? index-- : vertices);
            for (int it = 0; it <= vertices; it++) {
                if (neighbors.contains(it)) {
                    locations = (getLine(location, vertex.get(it--), density));
                }
            }
        }
        return locations;
    }

    private List<Location> getLine(Location start, Location end, double density) {
        List<Location> locations = new ArrayList<>();
        double points = start.distance(end);
        Vector delta = (start.toVector().subtract(end.toVector())).multiply(1/points);
        for (int i = 1; i <= points; i++) {
            locations.add(start.add(delta.multiply(new Vector(i, i, i))));
        }
        return locations;
    }
}

//!show happy villager at star at block 8 in front of player with 5 points, with radius 10 and density 5
