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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Core/Primary World")
@Description("Returns the main world, aka the fallback world.")
@Examples("set {_world} to the main world")
@Since("1.3.0")
public class ExprCoreWorld extends SimpleExpression<World> {

    static {
        Skript.registerExpression(ExprCoreWorld.class, World.class, ExpressionType.SIMPLE,
                "[the] (core|main|original|primary) world [of [the] server]");
    }

    @Nullable
    @Override
    protected World[] get(Event e) {
        return new World[]{Bukkit.getWorlds().get(0)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends World> getReturnType() {
        return World.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "core world";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }
}
