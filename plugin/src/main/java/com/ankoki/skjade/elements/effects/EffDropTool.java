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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Drop Item")
@Description("Makes the player drop one of their current item, or the whole stack.")
@Examples("make event-player drop their current item")
@Since("1.0.0")
public class EffDropTool extends Effect {

    static {
        Skript.registerEffect(EffDropTool.class,
                "make %players% drop (1¦[their] [current] item|2¦all [the] items in [their] hand|3¦[the] tool %itemstack%|their [(whole|entire)] inv[entory])");
    }

    private Expression<Player> player;
    private Expression<ItemStack> item;
    private boolean dropInv;
    private boolean dropStack;
    private boolean dropTool;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        if (p == null) return;
        if (!dropInv && !dropTool) {
            p.dropItem(dropStack);
            return;
        }
        if (dropInv) {
            p.getInventory().forEach(item -> {
                if (item != null) p.getWorld().dropItemNaturally(p.getLocation(), item);
            });
            p.getInventory().clear();
        }
        if (dropTool) {
            ItemStack i = item.getSingle(event);
            if (i == null || i.getType() == Material.AIR) return;
            p.getInventory().forEach(item -> {
                if (i == item) {
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                    p.getInventory().remove(item);
                    return;
                }
            });
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        dropInv = parseResult.mark == 0;
        dropStack = parseResult.mark == 1;
        dropTool = parseResult.mark == 3;
        if (dropTool) {
            item = (Expression<ItemStack>) exprs[1];
        }
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
