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
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Show Laser/Guardian Beam")
@Description("Shows a guardiam beam between two points.")
@Examples("show a laser from player to player's target block for 10 seconds for player")
@Since("1.3.1")
public class EffShowLaser extends Effect {

    static {
        if (SkJade.getInstance().isNmsEnabled())
            Skript.registerEffect(EffShowLaser.class,
                "show [a] [new] (la(s|z)er [beam]|guardian beam) from %location% to %location% for %timespan% [for %-players%]");
    }

    private Expression<Location> locationOneExpr, locationTwoExpr;
    private Expression<Timespan> timeExpr;
    private Expression<Player> playerExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        locationOneExpr = (Expression<Location>) exprs[0];
        locationTwoExpr = (Expression<Location>) exprs[1];
        timeExpr = (Expression<Timespan>) exprs[2];
        if (exprs.length > 3) playerExpr = (Expression<Player>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (locationOneExpr == null || locationTwoExpr == null || timeExpr == null) return;
        Location loc1 = locationOneExpr.getSingle(e);
        Location loc2 = locationTwoExpr.getSingle(e);
        Timespan sec = timeExpr.getSingle(e);
        if (loc1 == null || loc2 == null || sec == null) return;
        Player[] players;
        if (playerExpr != null) players = playerExpr.getArray(e);
        else players = loc1.getWorld().getPlayers().toArray(new Player[0]);
        int seconds = (int) Math.ceil(sec.getTicks_i() / 20D);
        try {
            Laser laser = new Laser.GuardianLaser(loc1, loc2, seconds, 100);
            laser.start(SkJade.getInstance(), players);
        } catch (ReflectiveOperationException ignore) {}
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "show a laser from " + locationOneExpr.toString(e, debug) + " to " + locationTwoExpr.toString(e, debug) + " for " +
                timeExpr.toString(e, debug) + (playerExpr == null ? "" : " for " + playerExpr.toString(e ,debug));
    }
}
