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
import com.ankoki.skjade.utils.Shapes;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Circle")
@Description("Returns the points of the outline of a circle which ")
@Examples("set all blocks at (circle at player's location with a radius of 5) to red wool")
@Since("1.0.0")
public class ExprCircle extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprCircle.class, Location.class, ExpressionType.SIMPLE,
                "[a[n]] (1¦upright|) circle (at|from) %location% with [a] radius [of] %number% [and %-number% total (points|blocks|locations)]");
    }

    private Expression<Location> centreExpr;
    private Expression<Number> radiusExpr;
    private Expression<Number> pointsExpr;
    private boolean upright;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        centreExpr = (Expression<Location>) exprs[0];
        radiusExpr = (Expression<Number>) exprs[1];
        pointsExpr = (Expression<Number>) exprs[2];
        upright = parseResult.mark == 1;
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location centre = centreExpr.getSingle(event);
        Number radiusNumber = radiusExpr.getSingle(event);
        if (centre == null || radiusNumber == null) return new Location[0];
        double radius = radiusNumber.doubleValue();
        double points;
        if (pointsExpr == null) points = radius * 10;
        else {
            Number pointsNumber = pointsExpr.getSingle(event);
            if (pointsNumber == null) return new Location[0];
            points = pointsNumber.doubleValue();
        }
        return upright ? Shapes.getUprightCircle(centre, radius, points).toArray(new Location[0]) :
                Shapes.getCircle(centre, radius, points).toArray(new Location[0]);
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
        return "circle with center " + centreExpr.toString(event, b) + " with radius " + radiusExpr.toString(event, b) +
                (pointsExpr == null ? "" : " and " + pointsExpr.toString(event, b) + "total points");
    }
}