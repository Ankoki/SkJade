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
        if (Laser.isEnabled())
            Skript.registerEffect(EffShowLaser.class,
                "show [a] [new] (la(s|z)er [beam]|guardian beam) from %location% to %location% for %timespan% [for %-players%]");
    }

    private Expression<Location> firstExpr, secondExpr;
    private Expression<Timespan> timeExpr;
    private Expression<Player> playerExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        firstExpr = (Expression<Location>) exprs[0];
        secondExpr = (Expression<Location>) exprs[1];
        timeExpr = (Expression<Timespan>) exprs[2];
        playerExpr = (Expression<Player>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Location first = firstExpr.getSingle(event);
        Location second = secondExpr.getSingle(event);
        Timespan sec = timeExpr.getSingle(event);
        if (first == null || second == null || sec == null) return;
        Player[] players = playerExpr == null ? first.getWorld().getPlayers().toArray(new Player[0]) : playerExpr.getArray(event);
        int seconds = (int) Math.ceil(sec.getTicks_i() / 20D);
        try {
            Laser laser = new Laser.GuardianLaser(first, second, seconds, 100);
            laser.start(SkJade.getInstance(), players);
        } catch (ReflectiveOperationException ignore) {}
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "show a laser from " + firstExpr.toString(e, debug) + " to " + secondExpr.toString(e, debug) + " for " +
                timeExpr.toString(e, debug) + (playerExpr == null ? "" : " for " + playerExpr.toString(e ,debug));
    }

}
