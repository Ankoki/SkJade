package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.ankoki.skjade.hooks.holograms.bukkitevents.HologramClickEvent;
import com.ankoki.skjade.hooks.holograms.bukkitevents.HologramTouchEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Hologram Line")
@Description("The hologram line of a specified hologram")
@Examples("set {_line::*} to all lines of the hologram with id \"hi\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class ExprHologramLine extends SimpleExpression<HologramLine> {

    static {
        Skript.registerExpression(ExprHologramLine.class, HologramLine.class, ExpressionType.PROPERTY,
                "[all] [the] lines of %holograms%",
                "line %number% of [the] [holo[gram]] %hologram%",
                "event( |-)[holo[gram]][(-| )]line");
    }

    private boolean single, inEvent;
    private Expression<Number> line;
    private Expression<Hologram> hologram;

    @Nullable
    @Override
    protected HologramLine[] get(Event event) {
        if (inEvent) {
            if (event instanceof HologramClickEvent) {
                return new HologramLine[]{((HologramClickEvent) event).getLine()};
            }
            else if (event instanceof HologramTouchEvent) {
                return new HologramLine[]{((HologramTouchEvent) event).getLine()};
            }
            return new HologramLine[0];
        }
        Hologram holo = hologram.getSingle(event);
        if (line != null) {
            int i = line.getSingle(event).intValue();
            i--;
            if (i < 0) i = 0;
            HologramLine line = holo.getLine(i);
            return line == null ? new HologramLine[0] :
                    new HologramLine[]{line};
        }
        return HologramManager.getLines(holo);
    }

    @Override
    public boolean isSingle() {
        return single;
    }

    @Override
    public Class<? extends HologramLine> getReturnType() {
        return HologramLine.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return inEvent ? "event-line" : (line == null ? "all the lines of " : "line " + line.toString(event, b) + " ") + " of " +
                hologram.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, ParseResult parseResult) {
        if (pattern == 2 && !ScriptLoader.isCurrentEvent(HologramClickEvent.class) && !ScriptLoader.isCurrentEvent(HologramTouchEvent.class)) {
            Skript.error("You cannot use event-hologram outside a hologram interact event!");
            return false;
        }
        if (pattern == 2) {
            inEvent = true;
            return true;
        }
        int i = 0;
        if (pattern == 1) {
            single = true;
            line = (Expression<Number>) exprs[i];
            i++;
        }
        hologram = (Expression<Hologram>) exprs[i];
        return true;
    }
}
