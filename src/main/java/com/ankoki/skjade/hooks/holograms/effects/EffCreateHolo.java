package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Create Hologram")
@Description("Creates a hologram at the specified location with the id specified. The ID must be unique.")
@Examples("create a hologram at location(10,100,10,world(\"world\")) with the id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EffCreateHolo extends Effect {

    static {
        Skript.registerEffect(EffCreateHolo.class,
                "create [a] [(hd|holographic[ ]displays)] holo[gram] at %location% [with [the]] id %string%");
    }

    private Expression<Location> location;
    private Expression<String> key;

    @Override
    protected void execute(Event event) {
        HologramManager.createHologram(key.getSingle(event), location.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create hologram";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        key = (Expression<String>) exprs[1];
        return true;
    }
}
