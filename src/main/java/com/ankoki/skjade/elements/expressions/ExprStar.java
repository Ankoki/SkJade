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

    private Expression<Location> centreExpr;
    private Expression<Number> pointsExpr;
    private Expression<Number> radiusExpr;
    private Expression<Number> densityExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        centreExpr = (Expression<Location>) exprs[0];
        pointsExpr = (Expression<Number>) exprs[1];
        radiusExpr = (Expression<Number>) exprs[2];
        densityExpr = (Expression<Number>) exprs[3];
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location centre = centreExpr.getSingle(event);
        Number pointsNumber = pointsExpr.getSingle(event);
        Number radiusNumber = radiusExpr.getSingle(event);
        Number densityNumber = densityExpr.getSingle(event);
        if (centre == null || pointsNumber == null || radiusNumber == null || densityNumber == null) return new Location[0];
        int points = pointsNumber.intValue();
        double radius = radiusNumber.doubleValue();
        double density = densityNumber.doubleValue();
        if (points < 2) return new Location[]{centre};
        return Shapes.getStar(centre, radius, density, points).toArray(new Location[0]);
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
    public String toString(@Nullable Event event, boolean debug) {
        return "star with center " + centreExpr.toString(event, debug) + " with radius " + radiusExpr.toString(event, debug) +
                " with density " + densityExpr.toString(event, debug);
    }
    
}