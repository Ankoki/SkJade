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
        return getStar(c, r, p, d).toArray(new Location[0]);
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

    private List<Location> getStar(Location center, double radius, int vertices, double density) {
        System.out.println("start");
        List<Location> locations = new ArrayList<>();
        double delta = _2PI / vertices;
        for (double theta = 0; theta < _2PI; theta += delta) {
            Vector offset = new Vector(Math.sin(theta) * radius, 0, Math.cos(theta) * radius);
            //gigi said suck it twink
            Location vertex = center;
            vertex.add(offset);
            locations.add(vertex);
            System.out.println("added the location " + center.add(offset).toString());
        }
        System.out.println("loop exited fam xxx");
        return locations;
    }
}

/*
!show happy villager at star at block 2 in front of player with 5 points, with radius 2 and density 5
!set blocks at (star at player's location with 5 points, with radius 2 and density 5) to red wool

 */
