package com.ankoki.skjade.elements.lasers.expressions;

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
import com.ankoki.skjade.elements.utils.LaserManager;
import com.ankoki.skjade.utils.Laser;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Laser/Guardian Beam")
@Description("Gets the laser/beam with the given id.")
@Examples("start the laser beam with id \"my laser\" for all players")
@Since("insert version")
public class ExprLaser extends SimpleExpression<Laser> {

    static {
        Skript.registerExpression(ExprLaser.class, Laser.class, ExpressionType.SIMPLE,
                "[the] (la(s|z)er [beam]|guardian beam) with [the] id %string%");
    }

    private Expression<String> string;

    @Nullable
    @Override
    protected Laser[] get(Event e) {
        if (string == null) return null;
        String id = string.getSingle(e);
        if (id == null || id.isEmpty()) return null;
        return new Laser[]{LaserManager.getLaser(id)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Laser> getReturnType() {
        return Laser.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the laser with the id " + string.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        string = (Expression<String>) exprs[0];
        return true;
    }
}
