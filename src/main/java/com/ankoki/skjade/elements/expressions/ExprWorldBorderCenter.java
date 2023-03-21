package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("World Border Center")
@Description("Allows you to get and set the center of a world border.")
@Examples("set the centre of player's world's world border to player's location")
@Since("1.3.0")
public class ExprWorldBorderCenter extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprWorldBorderCenter.class, Location.class, ExpressionType.SIMPLE,
                "[skjade] [world[ ]]border cent(re|er) of %world%",
                "[skjade] [the] %world%'s [world[ ]]border cent(re|er)",
                "[skjade] [the] cent(re|er) of %world%'s [world[ ]]border");
    }

    private Expression<World> worldExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event event) {
        World world = worldExpr.getSingle(event);
        if (world == null) return new Location[0];
        return new Location[]{world.getWorldBorder().getCenter()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the center of " + worldExpr.toString(e, debug) + "'s world border";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        return mode == ChangeMode.SET ? CollectionUtils.array(Location.class) : null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (delta.length < 1 || !(delta[0] instanceof Location) || mode != ChangeMode.SET) return;
        World w = worldExpr.getSingle(e);
        if (w == null) return;
        w.getWorldBorder().setCenter((Location) delta[0]);
    }

}
