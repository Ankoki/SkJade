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
import com.ankoki.skjade.utils.Utils;
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
                "[a] circle (at|from) %location% with [a] radius [of] %number% [and %-number% total (points|blocks|locations)]");
    }

    private Expression<Location> center;
    private Expression<Number> radius;
    private Expression<Number> total;

    @Nullable
    @Override
    protected Location[] get(Event event) {
        Location c = center.getSingle(event);
        double r = radius.getSingle(event).doubleValue();
        double t = total == null ? r * 10 : total.getSingle(event).doubleValue();
        if (c == null) return null;
        return Utils.getCircle(c, r, t).toArray(new Location[0]);
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
                (total == null ? "" : " and " + total.toString(event, b) + "total points");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        center = (Expression<Location>) exprs[0];
        radius = (Expression<Number>) exprs[1];
        total = (Expression<Number>) exprs[2];
        return true;
    }
}
