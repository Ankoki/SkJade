package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Unregister Placeholder")
@Description("Unregisters a Holographic Displays placeholder.")
@Examples("unregister holographic placeholder \"testPlaceholder\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class EffUnregisterPlaceholder extends Effect {

    static {
        Skript.registerEffect(EffUnregisterPlaceholder.class,
                "unregister [holo[grahpic[ displays]]] placeholder %string%");
    }

    private Expression<String> placeholder;

    @Override
    protected void execute(Event event) {
        String s = placeholder.getSingle(event);
        if (s == null) return;
        HologramsAPI.unregisterPlaceholder(SkJade.getInstance(), s);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "unregister holographic placeholder";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        placeholder = (Expression<String>) exprs[0];
        return true;
    }
}
