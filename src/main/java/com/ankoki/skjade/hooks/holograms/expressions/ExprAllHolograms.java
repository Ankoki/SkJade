package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Collection;

@Name("All Holograms")
@Description("Returns all holograms created by SkJade.")
@Examples("delete all skjade holograms")
@RequiredPlugins("Holographic Displays")
@Since("1.2.0")
public class ExprAllHolograms extends SimpleExpression<Hologram> {

    static {
        Skript.registerExpression(ExprAllHolograms.class, Hologram.class, ExpressionType.SIMPLE,
                "all [of] [the] [skjade[s]] holograms");
    }

    @Nullable
    @Override
    protected Hologram[] get(Event e) {
        Collection<Hologram> all = HologramsAPI.getHolograms(SkJade.getInstance());
        return all.toArray(new Hologram[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Hologram> getReturnType() {
        return Hologram.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all holograms";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }
}
