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
        Skript.registerExpression(ExprChunkAt.class, Chunk.class, ExpressionType.COMBINED,
                "chunk %number%(, | and) %number% of %world%");
    }

    private Expression<Number> chunkXExpr;
    private Expression<Number> chunkZExpr;
    private Expression<World> worldExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        chunkXExpr = (Expression<Number>) exprs[0];
        chunkZExpr = (Expression<Number>) exprs[1];
        worldExpr = (Expression<World>) exprs[2];
        return true;
    }

    @Override
    protected Chunk[] get(Event event) {
        Number chunkX = chunkXExpr.getSingle(event);
        Number chunkZ = chunkZExpr.getSingle(event);
        if (chunkX == null || chunkZ == null) return new Chunk[0];
        int x = chunkX.intValue();
        int z = chunkZ.intValue();
        World world = worldExpr.getSingle(event);
        if (world == null) return new Chunk[0];
        return new Chunk[]{world.getChunkAt(x, z)};
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
    public String toString(@Nullable Event event, boolean debug) {
        return "chunk at " + chunkXExpr.toString(event, debug) + ", " + chunkZExpr.toString(event, debug) + " in the world " + worldExpr.toString(event, debug);
    }

}
