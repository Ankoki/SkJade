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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Drop Item")
@Description("Makes the player drop one of their current item, or their inventory.")
@Examples("make event-player drop their current item")
@Since("1.0.0")
public class EffDropTool extends Effect {

    static {
        Skript.registerEffect(EffDropTool.class,
                "make %players% drop (1¦[their] [current] item|2¦all [the] items in [their] hand|their [(whole|entire)] inv[entory])");
    }

    private Expression<Player> playerExpr;
    private boolean dropInv;
    private boolean dropStack;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        dropInv = parseResult.mark == 0;
        dropStack = parseResult.mark == 1;
        playerExpr = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Player player = playerExpr.getSingle(event);
        if (player == null) return;
        if (!dropInv) {
            player.dropItem(dropStack);
            return;
        }
        player.getInventory().forEach(item -> {
            if (item != null) player.getWorld().dropItemNaturally(player.getLocation(), item);
        });
        player.getInventory().clear();
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "make " + playerExpr.toString(event, b)+ " drop " + (dropInv ? (dropStack ? " all items " : "") + " in their hand" : " their current inventory");
    }
}
