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
import org.jetbrains.annotations.Nullable;

@Name("Hardcore World")
@Description("Gets and sets wether or not a world is hardcore.")
@Examples("set the hardcore value of world \"world\" to true")
@Since("1.3.0")
public class ExprWorldHardcore extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprWorldHardcore.class, Boolean.class, ExpressionType.SIMPLE,
                        "[the] hardcore (value|mode|state) of %world%",
                        "[the] %world%'s hardcore (value|mode|state)");
    }

    private Expression<World> worldExpr;

    @Nullable
    @Override
    protected Boolean[] get(Event e) {
        if (worldExpr == null) return new Boolean[0];
        World world = worldExpr.getSingle(e);
        if (world == null) return new Boolean[0];
        return new Boolean[]{world.isHardcore()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "hardcore value of " + worldExpr.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldExpr = (Expression<World>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (delta[0] == null || !(delta[0] instanceof Boolean)) return;
        if (worldExpr == null) return;
        World world = worldExpr.getSingle(e);
        if (world == null) return;
        world.setHardcore((Boolean) delta[0]);
    }
}
