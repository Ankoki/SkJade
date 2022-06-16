package com.ankoki.skjade.elements.lasers.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.elements.lasers.LaserManager;
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Create Laser/Guardian Beam")
@Description("Creates a laser/guardian beam. Does NOT show it. For an infinite beam, use -1 seconds.")
@Examples("create a new laser from player to player's target block for 10 seconds with the id \"kachow\"")
@Since("1.3.1")
public class EffCreateLaser extends Effect {

    static {
        if (Laser.isEnabled())
            Skript.registerEffect(EffCreateLaser.class,
                "create [a] [new] (la(s|z)er [beam]|guardian beam) from %location% to %location% for %timespan% with [the] id %string%");
    }

    private Expression<Location> locationOneExpr, locationTwoExpr;
    private Expression<Timespan> timeExpr;
    private Expression<String> keyExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        locationOneExpr = (Expression<Location>) exprs[0];
        locationTwoExpr = (Expression<Location>) exprs[1];
        timeExpr = (Expression<Timespan>) exprs[2];
        keyExpr = (Expression<String>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (locationOneExpr == null || locationTwoExpr == null || timeExpr == null || keyExpr == null) return;
        Location loc1 = locationOneExpr.getSingle(e);
        Location loc2 = locationTwoExpr.getSingle(e);
        Timespan sec = timeExpr.getSingle(e);
        String id = keyExpr.getSingle(e);
        if (loc1 == null || loc2 == null || sec == null || id == null || id.isEmpty()) return;
        int seconds = (int) Math.ceil(sec.getTicks_i() / 20D);
        try {
            Laser laser = new Laser.GuardianLaser(loc1, loc2, seconds, 100);
            LaserManager.createLaser(id, laser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create a new laser from " + locationOneExpr.toString(e, debug) + " to " + locationTwoExpr.toString(e, debug) +
                " for " + timeExpr.toString(e, debug) + " with the id " + keyExpr.toString(e, debug);
    }
}
