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
import com.ankoki.skjade.utils.MinecraftVersion;
import com.ankoki.skjade.utils.ReflectionUtils;
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
    private Expression<Entity> entityExpr;
    private Expression<Player> playerExpr;

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffHideEntity.class,
                    "[skjade] (destroy|send [a] destroy packet for) [[the] entity] %entities% (1¦(from|for) %-players%|)");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutEntityDestroy");
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entityExpr = (Expression<Entity>) exprs[0];
        playerExpr = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (entityExpr == null) return;
        Entity[] entities = entityExpr.getArray(event);
        Player[] players = playerExpr == null ? Bukkit.getOnlinePlayers().toArray(new Player[0]) : playerExpr.getArray(event);
        for (Entity entity : entities) {
            try {
                Object instance = MinecraftVersion.v1_16_R4.isOlder() ? packet.getConstructor(int[].class).newInstance(new int[]{entity.getEntityId()}) :
                    packet.newInstance();
                if (MinecraftVersion.v1_17_R1.isNewer()) ReflectionUtils.setField(instance, "a", new int[]{entity.getEntityId()});
                for (Player player : players) ReflectionUtils.sendPacket(player, instance);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "destroy " + entityExpr.toString(e, debug) + (playerExpr == null ? "" : " from " + playerExpr.toString(e, debug));
    }
}
