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

@Name("Torus/Giant Donut")
@Description("Returns the locations to make up a giant donut/torus.")
@Examples("play green spark at a giant donut around player's location with major radius 5 and minor radius 2")
@Since("1.2.0")
public class ExprTorus extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprTorus.class, Location.class, ExpressionType.SIMPLE,
                "[a] (torus|[giant ]donut) (at|around) %location% with [a] major radius [of] %number% and [a] minor radius [of] %number% [with [a] density [of] %-number%]");
    }

    private Expression<Location> centreExpr;
    private Expression<Number> majorRadiusExpr;
    private Expression<Number> minorRadiusExpr;
    private Expression<Number> densityExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        centreExpr = (Expression<Location>) exprs[0];
        majorRadiusExpr = (Expression<Number>) exprs[1];
        minorRadiusExpr = (Expression<Number>) exprs[2];
        densityExpr = (Expression<Number>) exprs[3];
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location centre = centreExpr.getSingle(event);
        Number majorRadiusNumber = majorRadiusExpr.getSingle(event);
        Number minorRadiusNumber = minorRadiusExpr.getSingle(event);
        if (centre == null || majorRadiusNumber == null || minorRadiusNumber == null) return new Location[0];
        double major = majorRadiusNumber.doubleValue();
        double minor = minorRadiusNumber.doubleValue();
        double density;
        if (densityExpr == null) density = 1;
        else {
            Number densityNumber = densityExpr.getSingle(event);
            if (densityNumber == null) return new Location[0];
            density = densityNumber.doubleValue();
        }
        return Shapes.getTorus(centre, major, minor, density).toArray(new Location[0]);
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
    public String toString(@Nullable Event e, boolean debug) {
        return "torus around " + centreExpr.toString(e, debug) + " with a major radius of " + majorRadiusExpr.toString(e, debug) +
                " and a minor radius of " + minorRadiusExpr.toString(e, debug);
    }

}
