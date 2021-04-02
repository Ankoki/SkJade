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
import org.jetbrains.annotations.Nullable;

@Name("Cone")
@Description("Returns a list of locations to make up a cone.")
@Examples("show blue dust at cone around player, radius 5, height 8 and density 10")
@Since("1.2.0")
public class ExprCone extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprCone.class, Location.class, ExpressionType.SIMPLE,
                "[a] cone (with [a] cent(re|er) [of]|around) %location%(,| and) [a] radius [of] %number%[(,| and)] [a] height [of] %number%[(,| and)] [a] density [of] %number%");
    }

    private Expression<Location> centre;
    private Expression<Number> radius, height, density;

    @Nullable
    @Override
    protected Location[] get(Event e) {
        if (centre == null || radius == null || height == null || density == null) return null;
        Location l = centre.getSingle(e);
        double r = radius.getSingle(e).doubleValue();
        double h = height.getSingle(e).doubleValue();
        double d = density.getSingle(e).doubleValue();
        return Shapes.getCone(l, r, h, d, 100).toArray(new Location[0]);
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
        return "cone around " + centre.toString(e, debug) + ", a radius of " + radius.toString(e, debug) + ", height of " +
                height.toString(e, debug) + " and density of " + density.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        centre = (Expression<Location>) exprs[0];
        radius = (Expression<Number>) exprs[1];
        height = (Expression<Number>) exprs[2];
        density = (Expression<Number>) exprs[3];
        return true;
    }
}
