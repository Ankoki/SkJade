package com.ankoki.skjade.hooks.holograms.elements.effects.sub;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import com.ankoki.skjade.hooks.holograms.elements.sections.SecKeyedHologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Page")
@Description("Adds a new page (or default page) to the current hologram." +
        "This must be used within a hologram creation section.")
@Examples({"create new holo keyed \"stacyGURLS!\":",
        "\tpage 0: \"it's going\", \"DOWWWWWN\", glowing diamond sword and an ender dragon",
        "\tpersistent: false",
        "\tstatic: true",
        "\thide from: event-player"})
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class EffHoloPage extends Effect {

    static {
        HoloProvider provider = HoloHandler.get().getCurrentProvider();
        Skript.registerEffect(EffHoloPage.class,
                "(page %-number%|[default ]lines)\\: %strings/itemtypes" +
                        (provider.supportsEntityLines() ? "/entitytypes/entities%" : "%"));
        if (!provider.supportsPages())
            SkJade.getInstance().getLogger().warning("Please note your current hologram provider (" + provider.getId() + ") " +
                    "does not support the use of pages, so please make sure you do not try and create pages above index 0, otherwise " +
                    "the lines of your hologram will be replaced!");
        if (!provider.supportsEntityLines())
            SkJade.getInstance().getLogger().warning("Please note your current hologram provider (" + provider.getId() + ") " +
                    "does not support entity lines, so you will not be able to add entities or entity types to your holograms.");
    }

    private SecKeyedHologram section;
    private Expression<Number> pageExpr;
    private Expression<Object> linesExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        SkriptEvent event = getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class)) {
            section = (SecKeyedHologram) skriptEvent.getSection();
            pageExpr = (Expression<Number>) exprs[0];
            linesExpr = (Expression<Object>) exprs[1];
            return true;
        }
        Skript.error("You cannot edit a hologram outside of a section using this pattern.");
        return false;
    }

    @Override
    protected void execute(Event event) {
        int page = 0;
        if (pageExpr != null) {
            Number number = pageExpr.getSingle(event);
            if (number == null) return;
            page = HoloHandler.get().getCurrentProvider().supportsPages() ? number.intValue() : 0;
        }
        Object[] lines = linesExpr.getAll(event);
        SKJHoloBuilder builder = section.getCurrentBuilder();
        builder.addPage(page, lines);
        section.setCurrentBuilder(builder);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "page " + pageExpr.toString(event, b) + ": " + linesExpr.toString(event, b);
    }
}
