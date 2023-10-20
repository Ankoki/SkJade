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
@Since("1.0.0")
public class EffRemoveLine extends Effect {

    static {
        Skript.registerEffect(EffRemoveLine.class,
                "remove %hologramlines%",
                "remove [the] line %number% from %hologram%");
    }

    private Expression<Number> number;
    private Expression<Hologram> hologram;
    private Expression<HologramLine> hologramLine;
    private boolean isNumber;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        if (i == 1) {
            isNumber = true;
            number = (Expression<Number>) exprs[0];
            hologram = (Expression<Hologram>) exprs[1];
        } else hologramLine = (Expression<HologramLine>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (!isNumber) {
            if (hologramLine == null) return;
            HologramLine line = hologramLine.getSingle(event);
            if (line == null) return;
            HologramManager.removeLine(line);
            return;
        }
        if (hologram == null) return;
        Hologram holo = hologram.getSingle(event);
        Number num = number.getSingle(event);
        if (num == null) return;
        int i = num.intValue();
        if (holo == null) return;
        HologramManager.removeLine(holo, i);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return hologramLine == null ? "remove line " + number.toString(event, b) + "from the hologram " + hologram.toString(event, b)
                : "remove " + hologramLine.toString(event, b);
    }
}
