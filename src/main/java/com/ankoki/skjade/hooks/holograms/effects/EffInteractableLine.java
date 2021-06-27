package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.ankoki.skjade.hooks.holograms.HologramManager.TouchType;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Interactive Hologram Line")
@Description("Makes a line of a hologram clickable(triggers the on hologram click), touchable(triggers on hologram touch)," +
        "and interact(triggers on both)")
@Examples("make line 3 of the hologram with id \"hi\" clickable")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EffInteractableLine extends Effect {

    static {
        Skript.registerEffect(EffInteractableLine.class,
                "(make|set) [the] line %numbers% of %hologram% [to be] [(1¦(un|non[(-| )])|)]clickable",
                "(make|set) [the] line %numbers% of %hologram% [to be] [(1¦(un|non[(-| )])|)]touchable",
                "(make|set) [the] line %numbers% of %hologram% [to be] [(1¦(un|non[(-| )])|)]interactable");
    }

    private TouchType touchType;
    private boolean negated;
    private Expression<Number> lineNumber;
    private Expression<Hologram> hologram;

    @Override
    protected void execute(Event event) {
        Hologram holo = hologram.getSingle(event);
        Number num = lineNumber.getSingle(event);
        if (num == null) return;
        int i = num.intValue();
        HologramLine line = HologramManager.getLine(holo, i);
        if (holo == null || line == null) return;
        if (negated) {
            switch (touchType) {
                case CLICKABLE:
                    HologramManager.removeTouch(line);
                    break;
                case TOUCHABLE:
                    HologramManager.removePickup(line);
                    break;
                case INTERACTABLE:
                    HologramManager.removeTouch(line);
                    HologramManager.removePickup(line);
            }
            return;
        }
        switch (touchType) {
            case CLICKABLE:
                HologramManager.handleTouch(holo, line);
                break;
            case TOUCHABLE:
                HologramManager.handlePickup(holo, line);
                break;
            case INTERACTABLE:
                HologramManager.handlePickup(holo, line);
                HologramManager.handleTouch(holo, line);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "make line " + lineNumber.toString(event, b) + " of " + hologram.toString(event, b) + touchType.name().toLowerCase();
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        lineNumber = (Expression<Number>) exprs[0];
        hologram = (Expression<Hologram>) exprs[1];
        switch (i) {
            case 0:
                touchType = TouchType.CLICKABLE;
                break;
            case 1:
                touchType = TouchType.TOUCHABLE;
                break;
            case 2:
                touchType = TouchType.INTERACTABLE;
        }
        negated = parseResult.mark == 1;
        return true;
    }
}
