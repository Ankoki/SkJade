package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
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

//thank you pesekjan c:
@Name("Change the Sky Colour")
@Description("Changes the sky colour for players. The maximum number is 200 to make sure clients aren't crashed.")
@Examples("change the sky colour to 5 for all players")
@Since("1.1.0")
@RequiredPlugins("1.16+")
public class EffSkyColour extends Effect {

    private static Object h;
    private static Class<?> packet;
    private static Class<?> innerClass;
    private Expression<Number> numberExpr;
    private Expression<Player> playerExpr;

    static {
        if (SkJade.getInstance().isNmsEnabled() && Utils.getMinecraftMinor() >= 16) {
            Skript.registerEffect(EffSkyColour.class,
                    "change [the] sky colo[u]r to %number% for %players%");
            packet = ReflectionUtils.getNMSClass("network.protocol.game",
                    "PacketPlayOutGameStateChange");
            innerClass = packet.getDeclaredClasses()[0];
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        numberExpr = (Expression<Number>) exprs[0];
        playerExpr = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Number num = numberExpr.getSingle(e);
        if (num == null) return;
        float i = num.intValue();
        Player[] players = playerExpr.getArray(e);
        if (players.length <= 0) return;
        i = Math.min(i, 200);
        if (h == null) {
            try {
                h = innerClass.getConstructor(int.class)
                                .newInstance(7);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
                return;
            }
        }
        try {
            Object instance = packet.getConstructor(innerClass, float.class)
                    .newInstance(h, i);
            for (Player p : players) ReflectionUtils.sendPacket(p, instance);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "change the sky colour to " + numberExpr.toString(e, debug) + " for " + playerExpr.toString(e, debug);
    }
}
