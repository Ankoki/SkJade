package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("ID of a Hologram")
@Description("Returns the ID of a hologram.")
@Examples("broadcast event-hologram's id")
@Since("1.2.0")
public class ExprHologramId extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprHologramId.class, String.class, ExpressionType.SIMPLE,
                "([the] (id|key) of [the [hologram]] %-hologram%|%hologram%[']s (id|key))");
    }

    private Expression<Hologram> hologram;

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (hologram == null) return new String[0];
        Hologram holo = hologram.getSingle(e);
        if (holo == null) return new String[0];
        return new String[]{HologramManager.getIDFromHolo(holo)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return hologram.toString(e, debug) + "'s id";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hologram = (Expression<Hologram>) exprs[0];
        return true;
    }
}
