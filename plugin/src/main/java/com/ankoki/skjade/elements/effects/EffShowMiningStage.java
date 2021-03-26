package com.ankoki.skjade.elements.effects;

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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Show Mining Stage")
@Description("Shows the block break animation/stage to players.")
@Examples("show mining stage 5 at player's target block")
@Since("1.2.0")
public class EffShowMiningStage extends Effect {

    static {
        Skript.registerEffect(EffShowMiningStage.class,
                "(show|play) (mining|block break) (stage|animation) %number% at %locations% [to %-players%]",
                "remove [the] (mining|block break) (stage|animation) at %locations% [for %-players%]");
    }

    private Expression<Number> stage;
    private Expression<Location> location;
    private Expression<Player> players;
    private boolean remove = false;

    @Override
    protected void execute(Event e) {
        if (location == null) return;
        int i = 100;
        if (stage != null) {
            i = stage.getSingle(e).intValue();
        }
        Location[] locs = location.getArray(e);
        Player[] ps = players != null ? players.getArray(e) : Bukkit.getOnlinePlayers().toArray(new Player[0]);
        if (locs.length < 1) return;
        SkJade.getNmsHandler().showMiningStage(i, locs, ps, remove);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return remove ? "show mining stage " + stage.toString(e, debug) + " at " + location.toString(e, debug) + (players != null ? " to " + players.toString(e, debug) : "") :
                "remove the mining stage at " + location.toString(e, debug) + (players != null ? " for " + players.toString(e, debug) : "");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (matchedPattern == 0) {
            stage = (Expression<Number>) exprs[0];
            location = (Expression<Location>) exprs[1];
            if (exprs.length > 2) players = (Expression<Player>) exprs[2];
        } else {
            remove = true;
            location = (Expression<Location>) exprs[0];
            if (exprs.length > 1) players = (Expression<Player>) exprs[1];
        }
        return true;
    }
}
