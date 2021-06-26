package com.ankoki.skjade.elements.lasers.expressions;

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
import com.ankoki.skjade.utils.Laser;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Start Location of a Laser")
@Description("Returns the start or end location of a laser.")
@Examples("broadcast \"%the start location of the lazer with id \"\"i got a big batty\"%\"\"")
@Since("1.3.1")
public class ExprLaserPoints extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprLaserPoints.class, Location.class, ExpressionType.SIMPLE,
                "[the] (1¦start|end)[ing] [loc[ation]] of %laser%");
    }

    private Expression<Laser> laser;
    private boolean start;

    @Nullable
    @Override
    protected Location[] get(Event e) {
        if (laser == null) return new Location[0];
        Laser l = laser.getSingle(e);
        if (l == null) return new Location[0];
        return new Location[]{start ? l.getStart() : l.getEnd()};
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
        return "the starting location of " + laser.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laser = (Expression<Laser>) exprs[0];
        start = parseResult.mark == 1;
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(Location.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (delta.length < 1 || !(delta[0] instanceof Location)) return;
        Location location = (Location) delta[0];
        if (laser == null) return;
        Laser l = laser.getSingle(e);
        if (l == null) return;
        try {
            if (start) {
                l.moveStart(location);
            } else {
                l.moveEnd(location);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
