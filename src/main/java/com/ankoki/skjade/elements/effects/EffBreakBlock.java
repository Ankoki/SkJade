package com.ankoki.skjade.elements.effects;

import ch.njol.skript.ServerPlatform;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
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
@Description("Makes player break a block.")
@Examples("make player break event-block")
@Since("4.0.0")
@RequiredPlugins("Spigot. Doesn't work on Paper.")
public class EffBreakBlock extends Effect {

    private static Method breakBlock;

    static {
        if (Skript.methodExists(Player.class, "breakBlock", Block.class)) {
            try {
                breakBlock = Player.class.getMethod("breakBlock", Block.class);
            } catch (NoSuchMethodException ignored) {}
            Skript.registerEffect(EffBreakBlock.class,
                    "make %player% (mine|break) [the] [block [at]] %block/location%");
        }
    }

    private Expression<Player> playerExpr;
    private Expression<Object> objectExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        objectExpr = (Expression<Object>) exprs[1];
        return true;
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
