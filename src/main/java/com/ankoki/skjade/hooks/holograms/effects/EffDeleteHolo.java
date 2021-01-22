package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class EffDeleteHolo extends Effect {

    static {
        Skript.registerEffect(EffDeleteHolo.class,
                "delete [(hd|holographic[ ]displays)] holo[gram] [with [id]] %string%");
    }

    private Expression<String> key;

    @Override
    protected void execute(Event event) {
        HologramManager.deleteHologram(key.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "delete hologram";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        key = (Expression<String>) exprs[0];
        return true;
    }
}
