package com.ankoki.skjade.elements.effects;

import ch.njol.skript.ServerPlatform;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import jdk.jfr.Name;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.lang.reflect.Method;

@Name("Break Block")
public class EffBreakBlock extends Effect {

    private static Method breakBlock;

    static {
        if (Skript.getServerPlatform() == ServerPlatform.BUKKIT_SPIGOT) {
            try {
                breakBlock = Player.class.getMethod("breakBlock", Block.class);
            } catch (NoSuchMethodException ignored) {}
        }
        Skript.registerEffect(EffBreakBlock.class,
                "make %player% (mine|break) [the] block [at] %block/location%");
    }

    private Expression<Player> playerExpr;
    private Expression<Object> objectExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        if (Skript.getServerPlatform() == ServerPlatform.BUKKIT_SPIGOT) {
            playerExpr = (Expression<Player>) exprs[0];
            objectExpr = (Expression<Object>) exprs[1];
            return true;
        } else {
            Skript.error("Currently, this method only exists on Spigot, and not Paper.");
            return false;
        }
    }

    @Override
    protected void execute(Event event) {
        Player player = playerExpr.getSingle(event);
        Object object = objectExpr.getSingle(event);
        if (player == null || object == null) return;
        Block block;
        if (object instanceof Block) {
            block = (Block) object;
        } else {
            Location location = (Location) object;
            block = location.getBlock();
        }
        try {
            breakBlock.invoke(player, block);
        } catch (ReflectiveOperationException ignored) {}
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make " + playerExpr.toString(e, debug) + " break block at " + objectExpr.toString(e, debug);
    }
}
