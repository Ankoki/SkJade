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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Hide Entity")
@Description("Hides an entity from a player or all players.")
@Examples("hide player's target entity for all players")
@Since("1.3.0")
public class EffHideEntity extends Effect {

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffHideEntity.class,
                    "[skjade] (hide|destory|send [a] destroy packet for) [[the] entity] %entities% (1¦(from|for) %-players%|)");
        }
    }

    private static Class<?> packet = ReflectionUtils.getNMSClass("network.protocol.game",
            "PacketPlayOutEntityDestroy");
    private Expression<Entity> entity;
    private Expression<Player> playerExpr;

    @Override
    protected void execute(Event e) {
        if (entity == null) return;
        Entity[] entities = entity.getArray(e);
        Player[] players;
        if (playerExpr == null) {
            players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        }
        players = playerExpr.getArray(e);
        for (Entity entity : entities) {
            try {
                Object instance = packet.getConstructor(int.class)
                        .newInstance(entity.getEntityId());
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
        return "destroy " + entity.toString(e, debug) + (playerExpr == null ? "" : " from " + playerExpr.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entity = (Expression<Entity>) exprs[0];
        playerExpr = parseResult.mark == 1 ? (Expression<Player>) exprs[1] : null;
        return true;
    }
}
