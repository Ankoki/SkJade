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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Force Rain")
@Description("Make it start/stop raining for players.")
@Examples("make it stop raining for {queue::*}")
@Since("1.1.0")
public class EffForceRain extends Effect {

    private static Class<?> packet;
    private static Class<?> innerClass;
    private static Object c, b;
    private Expression<Player> playerExpr;
    private boolean rain;

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffForceRain.class,
                    "((1¦force [it] to rain|force it to stop raining)|make it (1¦|stop) rain[ing]) for %players%");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutGameStateChange");
            innerClass = packet.getDeclaredClasses()[0];
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        rain = parseResult.mark == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (playerExpr == null || Utils.getMinecraftMinor() < 16) return;
        Player[] players = playerExpr.getArray(event);
        if (c == null || b == null) {
            try {
                c = innerClass.getConstructor(int.class)
                        .newInstance(2);
                b = innerClass.getConstructor(int.class)
                        .newInstance(1);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
                return;
            }
        }
        try {
            Object instance = packet.getConstructor(innerClass, float.class)
                    .newInstance(rain ? c : b, 0F);
            for (Player p : players) {
                ReflectionUtils.sendPacket(p, instance);
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make it " + (rain ? "rain" : "stop raining") + " for " + playerExpr.toString(e, debug);
    }
}
