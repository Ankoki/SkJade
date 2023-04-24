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
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Start Location of a Laser")
@Description("Returns the start or end location of a laser.")
@Examples("broadcast \"%the start location of the lazer with id \"\"i got a big batty\"%\"\"")
@Since("1.3.1")
public class ExprLaserPoints extends SimpleExpression<Location> {

    static {
        if (Laser.isEnabled())
            Skript.registerExpression(ExprLaserPoints.class, Location.class, ExpressionType.COMBINED,
                "[the] (1¦start|end)[ing] [loc[ation]] of %laser%");
    }

    private Expression<Laser> laserExpr;
    private boolean start;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laserExpr = (Expression<Laser>) exprs[0];
        start = parseResult.mark == 1;
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event e) {
        if (laserExpr == null) return new Location[0];
        Laser laser = laserExpr.getSingle(e);
        if (laser == null) return new Location[0];
        return new Location[]{start ? laser.getStart() : laser.getEnd()};
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
        return "the starting location of " + laserExpr.toString(e, debug);
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
        if (laserExpr == null) return;
        Laser laser = laserExpr.getSingle(e);
        if (laser == null) return;
        try {
            if (start)
                laser.moveStart(location);
            else
                laser.moveEnd(location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
