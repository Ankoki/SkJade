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
import com.ankoki.skjade.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Show Mining Stage")
@Description("Shows the block break animation/stage to players.")
@Examples("show mining stage 5 at player's target block")
@Since("1.2.0")
public class EffShowMiningStage extends Effect {

    private static Class<?> packet;
    private static Class<?> blockPosition;

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffShowMiningStage.class,
                    "(show|play) (mining|block break) (stage|animation) %number% at %locations% [to %-players%] [(1¦with [the] [entity] id %-number%|)]",
                    "remove [the] (mining|block break) (stage|animation) at %locations% [for %-players%] [(1¦with [the] [entity] id %-number%|)]");
            blockPosition = ReflectionUtils.getNMSClass("core",
                    "BlockPosition");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutBlockBreakAnimation");
        }
    }

    private Expression<Number> stageExpr, idExpr;
    private Expression<Location> locationExpr;
    private Expression<Player> playerExpr;
    private boolean remove = false;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (matchedPattern == 0) {
            stageExpr = (Expression<Number>) exprs[0];
            locationExpr = (Expression<Location>) exprs[1];
            if (exprs.length == 3 && parseResult.mark == 1) idExpr = (Expression<Number>) exprs[2];
            else if (exprs.length == 3) playerExpr = (Expression<Player>) exprs[2];
            else if (exprs.length == 4) {
                playerExpr = (Expression<Player>) exprs[2];
                idExpr = (Expression<Number>) exprs[3];
            }
        } else {
            remove = true;
            locationExpr = (Expression<Location>) exprs[0];
            if (exprs.length > 1) playerExpr = (Expression<Player>) exprs[1];
            else if (parseResult.mark == 1) idExpr = (Expression<Number>) exprs[1];
            if (exprs.length == 3) idExpr = (Expression<Number>) exprs[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        int i = 100;
        if (stageExpr != null) {
            Number num = stageExpr.getSingle(event);
            if (num == null) return;
            i = num.intValue();
        }
        int ent = 0;
        if (idExpr != null) {
            Number num = idExpr.getSingle(event);
            if (num == null) return;
            ent = num.intValue();
        }
        Location[] locs = locationExpr.getArray(event);
        Player[] players = playerExpr != null ? playerExpr.getArray(event) : Bukkit.getOnlinePlayers().toArray(new Player[0]);
        if (locs.length < 1) return;
        int stage = Math.min(i, 9);
        stage = Math.max(stage, 0);
        if (remove) stage = 100;
        for (Location location : locs) {
            try {
                Object position;
                if (Utils.getMinecraftMinor() >= 19)
                    position = blockPosition.getConstructor(int.class, int.class, int.class)
                            .newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                else
                    position = blockPosition.getConstructor(double.class, double.class, double.class)
                            .newInstance(location.getX(), location.getY(), location.getZ());
                Object instance = packet.getConstructor(int.class, blockPosition, int.class)
                        .newInstance(ent, position, stage);
                for (Player p : players)
                    ReflectionUtils.sendPacket(p, instance);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return remove ? "remove the mining stage at " + locationExpr.toString(e, debug) + (playerExpr != null ? " for " + playerExpr.toString(e, debug) : "") :
                "show mining stage " + stageExpr.toString(e, debug) + " at " + locationExpr.toString(e, debug) + (playerExpr != null ? " to " + playerExpr.toString(e, debug) : "");
    }

}
