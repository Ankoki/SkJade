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
import com.ankoki.skjade.utils.Shapes;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Star")
@Description("Returns the points of the outline of a star from the center, radius, and density")
@Examples("set blocks at (star at player's location with 5 points, with radius 10 and density 5) to red wool")
@Since("1.0.0")
public class ExprStar extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprStar.class, Location.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] [(loc[ation]s|points) of] [a] star (at|from) %location% with %number% points(,| and) [with] [a] radius [of] %number%(,| and) [a] density [of] %number%");
    }

    private Expression<Location> center;
    private Expression<Number> points;
    private Expression<Number> radius;
    private Expression<Number> density;

    @Nullable
    @Override
    protected Location[] get(Event e) {
        Location c = center.getSingle(e);
        Number num1 = points.getSingle(e);
        Number num2 = radius.getSingle(e);
        Number num3 = density.getSingle(e);
        if (num1 == null || num2 == null || num3 == null) return new Location[0];
        int p = num1.intValue();
        double r = num2.doubleValue();
        double d = num3.doubleValue();
        if (c == null) return new Location[0];
        if (p < 2) return new Location[]{c};
        return Shapes.getStar(c, r, d, p).toArray(new Location[0]);
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
        points = (Expression<Number>) exprs[1];
        radius = (Expression<Number>) exprs[2];
        density = (Expression<Number>) exprs[3];
        return true;
    }
}