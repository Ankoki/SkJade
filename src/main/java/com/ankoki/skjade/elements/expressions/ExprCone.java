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

@Name("Cone")
@Description("Returns a list of locations to make up a cone.")
@Examples("show blue dust at cone around player, radius 5, height 8 and density 10")
@Since("1.2.0")
public class ExprCone extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprCone.class, Location.class, ExpressionType.SIMPLE,
                "[a] cone (with [a] cent(re|er) [of]|around) %location%(,| and) [a] radius [of] %number%[(,| and)] [a] height [of] %number%[(,| and)] [a] density [of] %number%");
    }

    private Expression<Location> centreExpr;
    private Expression<Number> radiusExpr, heightExpr, densityExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        centreExpr = (Expression<Location>) exprs[0];
        radiusExpr = (Expression<Number>) exprs[1];
        heightExpr = (Expression<Number>) exprs[2];
        densityExpr = (Expression<Number>) exprs[3];
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location centre = centreExpr.getSingle(event);
        Number radiusNumber = radiusExpr.getSingle(event);
        Number heightNumber = heightExpr.getSingle(event);
        Number densityNumber = densityExpr.getSingle(event);
        if (radiusNumber == null || heightNumber == null || densityNumber == null || centre == null) return new Location[0];
        double radius = radiusNumber.doubleValue();
        double height = heightNumber.doubleValue();
        double density = densityNumber.doubleValue();
        return Shapes.getCone(centre, radius, height, density, 100).toArray(new Location[0]);
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
        return "cone around " + centreExpr.toString(e, debug) + ", a radius of " + radiusExpr.toString(e, debug) + ", height of " +
                heightExpr.toString(e, debug) + " and density of " + densityExpr.toString(e, debug);
    }
}
