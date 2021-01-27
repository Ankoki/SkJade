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
import org.eclipse.jdt.annotation.Nullable;

@Name("Star")
@Description("Returns the points of the outline of a star from the center, radius, and density")
@Examples("show happy villager at star at block 8 in front of player with radius 10 and density 5")
@Since("1.0.0")
public class ExprStar extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprStar.class, Location.class, ExpressionType.SIMPLE,
                "[(all [[of] the]|the)] [(loc[ation]s|points) of] [a] star (at|from) %location% (with|of) [a] radius [of] %number%(,| and) [a] density [of] %number%");
    }

    private Expression<Location> center;
    private Expression<Number> radius;
    private Expression<Number> density;

    @Nullable
    @Override
    protected Location[] get(Event event) {
        //need to google this and have no wifi rn but maths is what
        /*
		loop 5 times:
			set {_p::%loop-number%} to expr-1 ~ (cylindrical vector expr-2, (loop-number * 72), 0)
		set {_l::*} to (3, 4, 4, 5 and 5)
		loop {_l::*}:
			set {_i} to (loop-index parsed as number)th element of (1, 1, 2, 2 and 3)
			set {_r::*} to {_r::*} and line between {_p::%{_i}%} and {_p::%loop-value%} with density expr-3
		return {_r::*}
         */
        return new Location[0];
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
        radius = (Expression<Number>) exprs[1];
        density = (Expression<Number>) exprs[2];
        return true;
    }
}
