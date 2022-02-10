package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Chunk at Location")
@Description("Returns the chunk in a world from the x and y coordinates.")
@Examples("set {_ch} to chunk 1, 3 of player's world")
@Since("1.0.0")
public class ExprChunkAt extends SimpleExpression<Chunk> {

    static {
        Skript.registerExpression(ExprChunkAt.class, Chunk.class, ExpressionType.SIMPLE,
                "chunk %number%(, | and) %number% of %world%");
    }

    private Expression<Number> chunkX;
    private Expression<Number> chunkZ;
    private Expression<World> world;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        chunkX = (Expression<Number>) exprs[0];
        chunkZ = (Expression<Number>) exprs[1];
        world = (Expression<World>) exprs[2];
        return true;
    }

    @Override
    protected Chunk[] get(Event event) {
        if (chunkX == null || chunkZ == null || world == null) return new Chunk[0];
        Number num1 = chunkX.getSingle(event);
        Number num2 = chunkZ.getSingle(event);
        if (num1 == null || num2 == null) return new Chunk[0];
        int x = num1.intValue();
        int z = num2.intValue();
        World w = world.getSingle(event);
        if (w == null) return new Chunk[0];
        Chunk chunk = w.getChunkAt(x, z);
        return new Chunk[]{chunk};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Chunk> getReturnType() {
        return Chunk.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "chunk at " + chunkX.toString(event, b) + ", " + chunkZ.toString(event, b) + " in the world " + world.toString(event, b);
    }
}
