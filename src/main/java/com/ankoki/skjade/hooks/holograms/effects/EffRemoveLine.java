package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Remove Line")
@Description("Removes a line from a hologram")
@Examples("remove line number 3 from the hologram with id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EffRemoveLine extends Effect {

    static {
        Skript.registerEffect(EffRemoveLine.class,
                "remove %hologramlines%",
                "remove [the] (%number%(st|nd|rd|th) line|line %number%) from %hologram%");
    }

    private Expression<Number> number;
    private Expression<Hologram> hologram;
    private Expression<HologramLine> hologramLine;
    private boolean is2nd;

    @Override
    protected void execute(Event event) {
        if (is2nd) {
            HologramLine line = hologramLine.getSingle(event);
            if (line == null) return;
            HologramManager.removeLine(line);
            return;
        }
        Hologram holo = hologram.getSingle(event);
        int i = number.getSingle(event).intValue();
        if (holo == null) return;
        HologramManager.removeLine(holo, i);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "remove line";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        if (parseResult.mark == 1) {
            is2nd = true;
            number = (Expression<Number>) exprs[0];
            hologram = (Expression<Hologram>) exprs[1];
        } else {
            hologramLine = (Expression<HologramLine>) exprs[0];
        }
        return true;
    }
}
