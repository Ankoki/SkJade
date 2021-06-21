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
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffShowMiningStage.class,
                    "(show|play) (mining|block break) (stage|animation) %number% at %locations% [to %-players%] [(1¦with [the] [entity] id %-number%|)]",
                    "remove [the] (mining|block break) (stage|animation) at %locations% [for %-players%] [(1¦with [the] [entity] id %-number%|)]");
        }
    }

    private Expression<Number> stage, entityId;
    private Expression<Location> location;
    private Expression<Player> players;
    private boolean remove = false;

    @Override
    protected void execute(Event e) {
        if (location == null) return;
        int i = 100;
        if (stage != null) {
            Number num = stage.getSingle(e);
            if (num == null) return;
            i = num.intValue();
        }
        int ent = 0;
        if (entityId != null) {
            Number num = entityId.getSingle(e);
            if (num == null) return;
            ent = num.intValue();
        }
        Location[] locs = location.getArray(e);
        Player[] ps = players != null ? players.getArray(e) : Bukkit.getOnlinePlayers().toArray(new Player[0]);
        if (locs.length < 1) return;
        SkJade.getInstance().getNmsHandler().showMiningStage(i, locs, ps, ent, remove);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return remove ? "remove the mining stage at " + location.toString(e, debug) + (players != null ? " for " + players.toString(e, debug) : "") :
                "show mining stage " + stage.toString(e, debug) + " at " + location.toString(e, debug) + (players != null ? " to " + players.toString(e, debug) : "");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (matchedPattern == 0) {
            stage = (Expression<Number>) exprs[0];
            location = (Expression<Location>) exprs[1];
            if (exprs.length == 3 && parseResult.mark == 1) {
                entityId = (Expression<Number>) exprs[2];
            } else if (exprs.length == 3) {
                players = (Expression<Player>) exprs[2];
            } else if (exprs.length == 4) {
                players = (Expression<Player>) exprs[2];
                entityId = (Expression<Number>) exprs[3];
            }
        } else {
            remove = true;
            location = (Expression<Location>) exprs[0];
            if (exprs.length > 1) {
                players = (Expression<Player>) exprs[1];
            } else {
                if (parseResult.mark == 1) entityId = (Expression<Number>) exprs[1];
            }
            if (exprs.length == 3) entityId = (Expression<Number>) exprs[2];
        }
        return true;
    }
}
