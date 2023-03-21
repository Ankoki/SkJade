package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Location is Within")
@Description("Checks if a location is between two locations")
@Examples("if player's location is within arg-1's location and arg-2's location:")
@Since("1.0.0")
public class CondLocIsWithin extends Condition {

    static {
        PropertyCondition.register(CondLocIsWithin.class,
                "within [[the] location] %location% and [[the] location] %location%", "locations");
    }

    private Expression<Location> firstExpr, secondExpr, thirdExpr;

    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        firstExpr = (Expression<Location>) exprs[0];
        secondExpr = (Expression<Location>) exprs[1];
        thirdExpr = (Expression<Location>) exprs[2];
        return true;
    }

    @Override
    public boolean check(Event event) {
        Location first = firstExpr.getSingle(event);
        Location second = secondExpr.getSingle(event);
        Location third = thirdExpr.getSingle(event);
        if (first == null || second == null || third == null) return false;
        return contains(first, second, third);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return firstExpr.toString(event, debug) + " is within " + secondExpr.toString(event, debug) + " and " + thirdExpr.toString(event, debug);
    }

    private boolean contains(Location first, Location second, Location third) {
        double x1 = Math.min(second.getX(), third.getX());
        double y1 = Math.min(second.getY(), third.getY());
        double z1 = Math.min(second.getZ(), third.getZ());
        double x2 = Math.max(second.getX(), third.getX());
        double y2 = Math.max(second.getY(), third.getY());
        double z2 = Math.max(second.getZ(), third.getZ());
        Location l1 = new Location(second.getWorld(), x1, y1, z1);
        Location l2 = new Location(second.getWorld(), x2, y2, z2);
        return first.getBlockX() >= l1.getBlockX() && first.getBlockX() <= l2.getBlockX()
                && first.getBlockY() >= l1.getBlockY() && first.getBlockY() <= l2.getBlockY()
                && first.getBlockZ() >= l1.getBlockZ() && first.getBlockZ() <= l2.getBlockZ();
    }

}