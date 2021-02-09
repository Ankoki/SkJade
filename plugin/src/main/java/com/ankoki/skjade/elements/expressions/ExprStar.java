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
@Examples("set blocks at (star at player's location with 5 points, with radius 10 and density 5) to red wool")
@Since("1.0.0")
public class ExprStar extends SimpleExpression<Location> {
    private static final double _2PI = 6.283185307179586;

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
        if (p < 2) return new Location[]{c};
        List<Location> points = getStarPoints(c, r, p);
        List<Location> allLines = new ArrayList<>();
        for (int i  = 0; i < (points.size() - 2); i++) {
            allLines.addAll(getLine(points.get(i), points.get(i + 2), 1 / d));
        }
        if (!isEven(p)) {
            allLines.addAll(getLine(points.get(points.size() - 1), points.get(1), 1 / d));
            allLines.addAll(getLine(points.get(points.size() - 2), points.get(0), 1 / d));
        } else {
            allLines.addAll(getLine(points.get(points.size() - 2), points.get(1), 1 / d));
        }
        /*
        int i = 0;
        for (Location loc : points) {
            Bukkit.broadcastMessage("Point " + i + ": " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
            i++;
        }*/
        return allLines.toArray(new Location[0]);
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

    private List<Location> getStarPoints(Location center, double radius, int vertices) {
        List<Location> locations = new ArrayList<>();
        double delta = _2PI / vertices;
        for (double theta = 0; theta < _2PI; theta += delta) {
            Vector offset = new Vector(Math.sin(theta) * radius, 0, Math.cos(theta) * radius);
            //gigi said suck it twink
            Location vertex = center.clone();
            vertex.add(offset);
            locations.add(vertex);
        }
        return locations;
    }

    private List<Location> getLine(Location loc1, Location loc2, double space) {
        List<Location> points = new ArrayList<>();
        double distance = loc1.distance(loc2);
        Vector p1 = loc1.toVector();
        Vector p2 = loc2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        for (double length = 0; length < distance; p1.add(vector)) {
            points.add(p1.toLocation(loc1.getWorld()));
            length += space;
        }
        return points;
    }

    private boolean isEven(int number) {
        return number % 2 == 0;
    }
}

/*
!show happy villager at star at block 2 in front of player with 5 points, with radius 2 and density 5
!set all blocks at (star at player's location with 6 points, with radius 20 and density 10) to red wool
 */
