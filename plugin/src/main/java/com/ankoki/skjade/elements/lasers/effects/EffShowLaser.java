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
import com.ankoki.skjade.utils.Laser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Show Laser/Guardian Beam")
@Description("Shows a guardiam beam between two points. For an infinite laser, use -1 seconds.")
@Examples("show a laser from player to player's target block for 10 seconds for player")
@Since("insert version")
public class EffShowLaser extends Effect {

    static {
        Skript.registerEffect(EffShowLaser.class,
                "show [a] [new] (la(s|z)er [beam]|guardian beam) from %location% to %location% for %timespan% [for %-players%]");
    }

    private Expression<Location> locationOne, locationTwo;
    private Expression<Timespan> time;
    private Expression<Player> allPlayers;

    @Override
    protected void execute(Event e) {
        if (locationOne == null || locationTwo == null || time == null) return;
        Location loc1 = locationOne.getSingle(e);
        Location loc2 = locationTwo.getSingle(e);
        Timespan sec = time.getSingle(e);
        if (loc1 == null || loc2 == null || sec == null) return;
        Player[] players;
        if (allPlayers != null) {
            players = allPlayers.getArray(e);
        } else {
            players = loc1.getWorld().getPlayers().toArray(new Player[0]);
        }
        int seconds = (int) Math.ceil(sec.getTicks_i() / 20D);
        try {
            Laser laser = new Laser(loc1, loc2, seconds, 100);
            laser.start(SkJade.getInstance(), players);
        } catch (Exception ex) {
            ex.printStackTrace(); //could probably ignore this after testing
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "show a laser from " + locationOne.toString(e, debug) + " to " + locationTwo.toString(e, debug) + " for " +
                time.toString(e, debug) + (allPlayers == null ? "" : " for " + allPlayers.toString(e ,debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        locationOne = (Expression<Location>) exprs[0];
        locationTwo = (Expression<Location>) exprs[1];
        time = (Expression<Timespan>) exprs[2];
        if (exprs.length > 3) allPlayers = (Expression<Player>) exprs[3];
        return true;
    }
}
