package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Add Item Line")
@Description("Adds an item to the hologram.")
@Examples("add glowing diamond to the hologram with id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class EffAddItemLine extends Effect {

    static {
        Skript.registerEffect(EffAddItemLine.class,
                "add [[the] item] %itemstack% to %skjhologram%");
    }

    private Expression<ItemStack> item;
    private Expression<Hologram> hologram;

    @Override
    protected void execute(Event event) {
        Hologram holo = hologram.getSingle(event);
        ItemStack i = item.getSingle(event);
        if (holo == null || i == null) return;
        HologramManager.addItemLine(holo, i);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "add item line " + item.toString(event, b) + " to " + hologram.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        item = (Expression<ItemStack>) exprs[0];
        hologram = (Expression<Hologram>) exprs[1];
        return true;
    }
}
