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
import com.ankoki.skjade.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hide Entity")
@Description("Hides an entity from a player or all players.")
@Examples("hide player's target entity for all players")
@Since("1.3.0")
public class EffHideEntity extends Effect {

    private static Class<?> packet;
    private Expression<Entity> entity;
    private Expression<Player> playerExpr;

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffHideEntity.class,
                    "[skjade] (hide|destroy|send [a] destroy packet for) [[the] entity] %entities% (1¦(from|for) %-players%|)");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutEntityDestroy");
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entity = (Expression<Entity>) exprs[0];
        playerExpr = parseResult.mark == 1 ? (Expression<Player>) exprs[1] : null;
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (entity == null) return;
        Entity[] entities = entity.getArray(e);
        Player[] players;
        if (playerExpr == null) {
            players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        } else {
            players = playerExpr.getArray(e);
        }
        for (Entity entity : entities) {
            try {
                Object instance = Version.v1_16_R4.isOlder() ? packet.getConstructor(int[].class).newInstance(new int[]{entity.getEntityId()}) :
                    packet.newInstance();
                if (Version.v1_17_R1.isNewer()) ReflectionUtils.setField(instance, "a", new int[]{entity.getEntityId()});
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
}
