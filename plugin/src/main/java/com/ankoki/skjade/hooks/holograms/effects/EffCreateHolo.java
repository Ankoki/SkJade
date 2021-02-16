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
@Description({"Creates a hologram at the specified location with the id specified. The ID must be unique.",
              "IMPORTANT: Holograms are not persistant over restart (for the moment), so make sure to recreate the holograms on script load!"})
@Examples("create a hologram at location(10,100,10,world(\"world\")) with the id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class EffCreateHolo extends Effect {

    static {
        Skript.registerEffect(EffCreateHolo.class,
                "create [a] [(hd|holographic[ ]displays)] holo[gram] at %location% [with [the]] id %string% (1¦to be (hidden|invisible) [by default]|) (2¦[and] [to] not (allow|accept|show|translate) placeholders|)");
    }

    private Expression<Location> location;
    private Expression<String> key;
    private boolean hidden, notAccepting;

    @Override
    protected void execute(Event event) {
        String k = key.getSingle(event);
        Location loc = location.getSingle(event);
        if (k == null || loc == null) return;
A        HologramManager.createHologram(k, loc, !hidden, !notAccepting);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create hologram at " + location.toString(event, b) + " with the id " + key.toString(event, b) + (hidden ? " to be hidden " : "") + (notAccepting ? "to not allow placeholders" : "");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        key = (Expression<String>) exprs[1];
        hidden = parseResult.mark == 1 || parseResult.mark == 3;
        notAccepting = parseResult.mark == 2 || parseResult.mark == 3;
        return true;
    }
}
