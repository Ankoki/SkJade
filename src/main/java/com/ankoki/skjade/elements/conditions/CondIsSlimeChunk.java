package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.Chunk;

@Name("Slime Chunk")
@Description("Checks if a chunk is a slime chunk.")
@Examples("if {_chunk} is a slime chunk:")
@Since("1.0.0")
public class CondIsSlimeChunk extends PropertyCondition<Chunk> {

    static {
        register(CondIsSlimeChunk.class, "slime(y|chunk)", "chunks");
    }

    @Override
    public boolean check(Chunk chunk) {
        return chunk.isSlimeChunk();
    }

    @Override
    protected String getPropertyName() {
        return "slime chunk";
    }

}
