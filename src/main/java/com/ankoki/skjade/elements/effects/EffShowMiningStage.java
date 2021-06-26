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
import com.ankoki.skjade.utils.ReflectionUtils;
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

    private static Class<?> packet = ReflectionUtils.getNMSClass("network.protocol.game",
            "PacketPlayOutBlockBreakAnimation");
    private static Class<?> blockPosition = ReflectionUtils.getNMSClass("core",
            "BlockPosition");
    private Expression<Number> stageExpr, entityId;
    private Expression<Location> location;
    private Expression<Player> playerExpr;
    private boolean remove = false;

    @Override
    protected void execute(Event e) {
        if (location == null) return;
        int i = 100;
        if (stageExpr != null) {
            Number num = stageExpr.getSingle(e);
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
        Player[] players = playerExpr != null ? playerExpr.getArray(e) : Bukkit.getOnlinePlayers().toArray(new Player[0]);
        if (locs.length < 1) return;
        int stage = Math.min(i, 9);
        stage = Math.max(stage, 0);
        if (remove) stage = 100;
        for (Location location : locs) {
            try {
                Object position = blockPosition.getConstructor(double.class, double.class, double.class)
                        .newInstance(location.getX(), location.getY(), location.getZ());
                Object instance = packet.getConstructor(int.class, blockPosition, int.class)
                        .newInstance(ent, position, stage);
                for (Player p : players) {
                    ReflectionUtils.sendPacket(p, instance);
                }
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return remove ? "remove the mining stage at " + location.toString(e, debug) + (playerExpr != null ? " for " + playerExpr.toString(e, debug) : "") :
                "show mining stage " + stageExpr.toString(e, debug) + " at " + location.toString(e, debug) + (playerExpr != null ? " to " + playerExpr.toString(e, debug) : "");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (matchedPattern == 0) {
            stageExpr = (Expression<Number>) exprs[0];
            location = (Expression<Location>) exprs[1];
            if (exprs.length == 3 && parseResult.mark == 1) {
                entityId = (Expression<Number>) exprs[2];
            } else if (exprs.length == 3) {
                playerExpr = (Expression<Player>) exprs[2];
            } else if (exprs.length == 4) {
                playerExpr = (Expression<Player>) exprs[2];
                entityId = (Expression<Number>) exprs[3];
            }
        } else {
            remove = true;
            location = (Expression<Location>) exprs[0];
            if (exprs.length > 1) {
                playerExpr = (Expression<Player>) exprs[1];
            } else {
                if (parseResult.mark == 1) entityId = (Expression<Number>) exprs[1];
            }
            if (exprs.length == 3) entityId = (Expression<Number>) exprs[2];
        }
        return true;
    }
}
