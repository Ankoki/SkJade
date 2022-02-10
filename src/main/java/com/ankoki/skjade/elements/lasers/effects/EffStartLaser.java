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

    private Expression<Laser> laserExpr;
    private Expression<Player> playerExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        laserExpr = (Expression<Laser>) exprs[0];
        if (exprs.length > 1) playerExpr = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (laserExpr == null) return;
        Laser laser = laserExpr.getSingle(e);
        if (laser == null || laser.isStarted()) return;
        Player[] players;
        if (playerExpr == null) {
            players = laser.getStart().getWorld().getPlayers().toArray(new Player[0]);
        } else {
            players = playerExpr.getArray(e);
        }
        laser.start(SkJade.getInstance(), players);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "start " + laserExpr.toString(e, debug) + (playerExpr == null ? "" : " for " + playerExpr.toString(e, debug));
    }
}
