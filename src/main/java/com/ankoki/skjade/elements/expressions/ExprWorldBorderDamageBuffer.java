package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("World Border Damage Buffer")
@Description("Allows you to get and set the damage buffer of a world border.")
@Examples("set the damage buffer of player's world's world border to 3")
@Since("1.3.0")
public class ExprWorldBorderDamageBuffer extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprWorldBorderDamageBuffer.class, Number.class, ExpressionType.SIMPLE,
                "[skjade] [world[ ]]border damage buffer of %world%",
                "[skjade] %world%'s [world[ ]]border damage buffer",
                "[skjade] [the] damage buffer of %world%'s [world[ ]]border");
    }

    private Expression<World> worldExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event e) {
        if (worldExpr == null) return new Number[0];
        World world = worldExpr.getSingle(e);
        if (world == null) return new Number[0];
        return new Number[]{world.getWorldBorder().getDamageBuffer()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return worldExpr.toString(e, debug) + "'s world border damage buffer";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.REMOVE_ALL) return null;
        return CollectionUtils.array(Number.class);
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        World world = worldExpr.getSingle(e);
        if (world == null) return;
        if (mode == ChangeMode.ADD || mode == ChangeMode.SET || mode == ChangeMode.REMOVE) {
            if (delta.length < 1 || !(delta[0] instanceof Number)) return;
            Number number = (Number) delta[0];
            double i = number.doubleValue();
            double currentBuffer = world.getWorldBorder().getDamageBuffer();
            if (mode == ChangeMode.ADD) {
                world.getWorldBorder().setDamageBuffer(Math.max(0, currentBuffer + i));
            } else if (mode == ChangeMode.REMOVE) {
                world.getWorldBorder().setDamageBuffer(Math.max(0, currentBuffer - i));
            }  else {
                world.getWorldBorder().setDamageBuffer(Math.max(0, i));
            }
            return;
        }
        world.getWorldBorder().setDamageBuffer(0);
    }
}
