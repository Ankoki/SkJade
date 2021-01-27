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
import com.ankoki.skjade.SkJade;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Refresh Chunks")
@Description("Refeshes the chunks given asynchronously.")
@Examples("refresh the chunk at arg-1")
@Since("1.0.0")
public class EffRefreshChunks extends Effect {

    static {
        Skript.registerEffect(EffRefreshChunks.class,
                "(reload|refresh) [the] [chunk[s]] %chunks%");
    }

    private Expression<Chunk> chunks;

    @Override
    protected void execute(Event event) {
        if (chunks == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(SkJade.getInstance(), () -> {
            Arrays.stream(chunks.getArray(event)).forEach(chunk -> {
                if (chunk == null) return;
                chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
            });
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "refresh chunks " + chunks.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        return true;
    }
}
