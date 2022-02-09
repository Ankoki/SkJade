package com.ankoki.skjade.elements.lasers.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Start Laser/Beam")
@Description("Starts a stored laser.")
@Examples("start the laser with id \"nicki minaj is the queen of rap\"")
@Since("1.3.1")
public class EffStartLaser extends Effect {

    static {
        if (SkJade.getInstance().isNmsEnabled())
            Skript.registerEffect(EffStartLaser.class,
                "start %laser% [for %-players%]");
    }

    private Expression<Laser> laser;
    private Expression<Player> allPlayers;

    @Override
    protected void execute(Event e) {
        if (laser == null) return;
        Laser l = laser.getSingle(e);
        if (l == null || l.isStarted()) return;
        Player[] players;
        if (allPlayers == null) {
            players = l.getStart().getWorld().getPlayers().toArray(new Player[0]);
        } else {
            players = allPlayers.getArray(e);
        }
        l.start(SkJade.getInstance(), players);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "start " + laser.toString(e, debug) + (allPlayers == null ? "" : " for " + allPlayers.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laser = (Expression<Laser>) exprs[0];
        if (exprs.length > 1) allPlayers = (Expression<Player>) exprs[1];
        return true;
    }
}
