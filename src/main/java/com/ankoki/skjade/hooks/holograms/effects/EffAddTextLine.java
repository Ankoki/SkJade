package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Add Text Line")
@Description("Adds a line of text to the hologram.")
@Examples("add line \"This is a hologram!\" to the hologram with id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class EffAddTextLine extends Effect {

    static {
        Skript.registerEffect(EffAddTextLine.class,
                "add [the] [text] [line[s]] %strings% to %skjhologram%");
    }

    private Expression<String> line;
    private Expression<Hologram> hologram;

    @Override
    protected void execute(Event event) {
        Hologram holo = hologram.getSingle(event);
        String[] text = line.getArray(event);
        if (holo == null || text.length < 1) return;
        Arrays.stream(text).forEach(l -> HologramManager.addTextLine(holo, l));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "add text line " + line.toString(event, b) + " to " + hologram.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        line = (Expression<String>) exprs[0];
        hologram = (Expression<Hologram>) exprs[1];
        return true;
    }
}
