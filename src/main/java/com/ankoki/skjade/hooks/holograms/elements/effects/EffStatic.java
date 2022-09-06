package com.ankoki.skjade.hooks.holograms.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.HoloManager;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import com.ankoki.skjade.hooks.holograms.elements.sections.SecKeyedHologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class EffStatic extends Effect {

    static {
        HoloProvider provider = HoloManager.get().getCurrentProvider();
        if (provider.supportsStatic()) Skript.registerEffect(EffVisibility.class,
                "static\\: %boolean%");
        else
            SkJade.getInstance().getLogger().warning("Please note your current hologram provider (" + provider.getId() + ") " +
                    "does not support the use of static holograms! You cannot use the static effect.");
    }

    private SecKeyedHologram section;
    private Expression<Boolean> staticExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        SkriptEvent event = getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class)) {
            section = (SecKeyedHologram) skriptEvent.getSection();
            staticExpr = (Expression<Boolean>) exprs[0];
            return true;
        }
        Skript.error("You cannot add lines or pages to a hologram outside of a section using this pattern.");
        return false;
    }

    @Override
    protected void execute(Event event) {
        Boolean stat = staticExpr.getSingle(event);
        if (stat == null) return;
        SKJHoloBuilder builder = section.getCurrentBuilder();
        builder.setStatic(stat);
        section.setCurrentBuilder(builder);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "static: " + staticExpr.toString(event, b);
    }
}