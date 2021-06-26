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

    private Expression<Location> location, locationOne, locationTwo;

    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        locationOne = (Expression<Location>) exprs[1];
        locationTwo = (Expression<Location>) exprs[2];
        return true;
    }

    @Override
    public boolean check(Event event) {
        Location loc = location.getSingle(event);
        Location loc1 = locationOne.getSingle(event);
        Location loc2 = locationTwo.getSingle(event);
        if (loc == null || loc1 == null || loc2 == null) return false;
        return contains(loc, loc1, loc2);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return location.toString(event, b) + " is within " + locationOne.toString(event, b) + " and " + locationTwo.toString(event, b);
    }

    private boolean contains(Location loc, Location loc1, Location loc2) {
        double x1 = Math.min(loc1.getX(), loc2.getX());
        double y1 = Math.min(loc1.getY(), loc2.getY());
        double z1 = Math.min(loc1.getZ(), loc2.getZ());
        double x2 = Math.max(loc1.getX(), loc2.getX());
        double y2 = Math.max(loc1.getY(), loc2.getY());
        double z2 = Math.max(loc1.getZ(), loc2.getZ());
        Location l1 = new Location(loc1.getWorld(), x1, y1, z1);
        Location l2 = new Location(loc1.getWorld(), x2, y2, z2);
        return loc.getBlockX() >= l1.getBlockX() && loc.getBlockX() <= l2.getBlockX()
                && loc.getBlockY() >= l1.getBlockY() && loc.getBlockY() <= l2.getBlockY()
                && loc.getBlockZ() >= l1.getBlockZ() && loc.getBlockZ() <= l2.getBlockZ();
    }
}