package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Add Item Line")
@Description("Adds an item to the hologram.")
@Examples("add a glowing diamond to the hologram with id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EffAddItemLine extends Effect {

    static {
        Skript.registerEffect(EffAddItemLine.class,
                "add [[the] item] %itemstack% to [[the] hologram] [with [the]] id %string%");
    }

    Expression<ItemStack> item;
    Expression<String> key;

    @Override
    protected void execute(Event event) {
        HologramManager.addItemLine(key.getSingle(event), item.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "add item line";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        item = (Expression<ItemStack>) exprs[0];
        key = (Expression<String>) exprs[1];
        return true;
    }
}
