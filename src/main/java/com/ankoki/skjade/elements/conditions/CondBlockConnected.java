package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import jdk.jfr.Name;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Break Block")
@Description("Makes player break a block.")
@Examples("make player break event-block")
@Since("1.4.0")
public class CondBlockConnected extends Condition {

    static {
        Skript.registerCondition(CondBlockConnected.class,
                "%block% is (connected to|exposed to|touching) %itemtype%");
    }

    private Expression<Block> blockExpr;
    private Expression<ItemType> materialExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        blockExpr = (Expression<Block>) exprs[0];
        materialExpr = (Expression<ItemType>) exprs[1];
        return true;
    }

    @Override
    public boolean check(Event event) {
        Block block = blockExpr.getSingle(event);
        ItemType itemtype = materialExpr.getSingle(event);
        if (block == null || itemtype == null) return false;
        Material material = itemtype.getMaterial();
        Location location = block.getLocation();
        World world = block.getWorld();
        if (world.getBlockAt(location.clone().add(1, 0, 0)).getType() == material) return true;
        if (world.getBlockAt(location.clone().add(0, 1, 0)).getType() == material) return true;
        if (world.getBlockAt(location.clone().add(0, 0, 1)).getType() == material) return true;
        if (world.getBlockAt(location.clone().subtract(1, 0, 0)).getType() == material) return true;
        if (world.getBlockAt(location.clone().subtract(0, 1, 0)).getType() == material) return true;
        return world.getBlockAt(location.clone().subtract(0, 0, 1)).getType() == material;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return blockExpr.toString(e, debug) + " is touching " + materialExpr.toString(e, debug);
    }
}
