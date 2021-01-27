package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Chunk at Location")
@Description("Returns the chunk in a world from the x and y coordinates.")
@Examples("set {_ch} to chunk 1, 3 of player's world")
@Since("1.0.0")
public class ExprChunkAt extends PropertyExpression<World, Chunk> {

    static {
        Skript.registerExpression(ExprChunkAt.class, Chunk.class, ExpressionType.COMBINED,
                "chunk %number%(, | and) %number%");
    }

    private Expression<Number> chunkX;
    private Expression<Number> chunkZ;

    @Override
    protected Chunk[] get(Event event, World[] worlds) {
        return new Chunk[]{worlds[0].getChunkAt(chunkX.getSingle(event).intValue(), chunkZ.getSingle(event).intValue())};
    }

    @Override
    public Class<? extends Chunk> getReturnType() {
        return Chunk.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "chunk at " + chunkX.toString(event, b) + ", " + chunkZ.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        chunkX = (Expression<Number>) exprs[0];
        chunkZ = (Expression<Number>) exprs[1];
        return true;
    }
}
