package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.ankoki.skjade.utils.events.HologramClickEvent;
import com.ankoki.skjade.utils.events.HologramTouchEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram")
@Description("A HolographicDisplays Hologram. The ID of every hologram should be unique.")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class ExprHologram extends SimpleExpression<Hologram> {

    static {
        Skript.registerExpression(ExprHologram.class, Hologram.class, ExpressionType.PROPERTY,
                "[the] holo[gram] with [the] id %string%",
                "event(-| )holo[gram]");
    }

    private Expression<String> key;
    private boolean inEvent;

    @Nullable
    @Override
    protected Hologram[] get(Event event) {
        if (inEvent) {
            return new Hologram[]{getFromEvent(event)};
        }
        return new Hologram[]{HologramManager.getHologram(key.getSingle(event))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Hologram> getReturnType() {
        return Hologram.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "hologram";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        if (i == 1 && !ScriptLoader.isCurrentEvent(HologramClickEvent.class) && !ScriptLoader.isCurrentEvent(HologramTouchEvent.class)) {
            Skript.error("You cannot use event-hologram outside a hologram interact event!");
            return false;
        }
        if (i == 1) {
            inEvent = true;
            return true;
        }
        key = (Expression<String>) exprs[0];
        return true;
    }

    private Hologram getFromEvent(Event e) {
        if (e instanceof HologramClickEvent) {
            return ((HologramClickEvent) e).getHologram();
        }
        else if (e instanceof HologramTouchEvent) {
            return ((HologramTouchEvent) e).getHologram();
        }
        return null;
    }
}
