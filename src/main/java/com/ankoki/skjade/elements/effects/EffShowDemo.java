package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Version;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.utils.ReflectionUtils;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Show Demo")
@Description("Shows the minecraft demonstration screen to a player.")
@Examples("show demo screen to event-player")
@Since("1.0.0")
public class EffShowDemo extends Effect {

    private static Class<?> packet;
    private static Class<?> innerClass;
    private Expression<Player> playersExpr;
    private static Object f;

    static {
        if (SkJade.getInstance().isNmsEnabled() &&
                new Version(String.valueOf(Skript.getMinecraftVersion())).isLargerThan(new Version("1.13.2"))) {
            Skript.registerEffect(EffShowDemo.class,
                    "show [the] demo[nstration] screen to %players%");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutGameStateChange");
            innerClass = packet.getDeclaredClasses()[0];
        }
    }

    @Override
    protected void execute(Event event) {
        Player[] players = playersExpr.getArray(event);
        if (f == null) {
            if (Utils.getServerMajorVersion() < 16) {
                try {
                    Object instance = packet.getConstructor(int.class, float.class)
                            .newInstance(5, 0F);
                    for (Player p : players) {
                        ReflectionUtils.sendPacket(p, instance);
                    }
                } catch (ReflectiveOperationException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            try {
                f = innerClass.getConstructor(int.class)
                                .newInstance(5);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
                return;
            }
        }
        try {
            Object instance = packet.getConstructor(innerClass, float.class)
                    .newInstance(f, 0F);
            for (Player p : players) {
                ReflectionUtils.sendPacket(p, instance);
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "show the demo screen to " + playersExpr.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        playersExpr = (Expression<Player>) exprs[0];
        return true;
    }
}
