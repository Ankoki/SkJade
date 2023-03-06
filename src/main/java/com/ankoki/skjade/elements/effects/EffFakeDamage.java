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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Fake Damage")
@Description("Makes a player look like they took damage.")
@Examples("make event-player take fake damage")
@Since("1.0.0")
public class EffFakeDamage extends Effect {

    private static Class<?> packet;
    private Expression<Player> damagedExpr;
    private Expression<Player> viewerExpr;

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffFakeDamage.class,
                    "make %players% take fake damage [for %-players%]");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutAnimation");
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        damagedExpr = (Expression<Player>) exprs[0];
        viewerExpr = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Player[] viewers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        if (viewerExpr != null) viewers = viewerExpr.getArray(e);
        Player[] damaged = damagedExpr.getArray(e);
        for (Player player : damaged) {
            try {
                Object instance;
                if (Version.v1_18_R2.isOlder()) {
                    Object handle = ReflectionUtils.getHandle(player);
                    instance = packet.getConstructor(handle.getClass().getSuperclass().getSuperclass().getSuperclass(), int.class).newInstance(handle, 1);
                } else {
                    instance = packet.getConstructor().newInstance();
                    ReflectionUtils.setField(instance, "a", player.getEntityId());
                    ReflectionUtils.setField(instance, "b", 1);
                }
                for (Player viewer : viewers) {
                    ReflectionUtils.sendPacket(viewer, instance);
                }
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make " + damagedExpr.toString(e, debug) + " take fake damage for " + (viewerExpr == null ? "all players" : viewerExpr.toString(e, debug));
    }
}
