package com.ankoki.skjade.hooks.holograms.elements.effects.sub;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import com.ankoki.skjade.hooks.holograms.elements.sections.SecKeyedHologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Static")
@Description("Currently does nothing. When DecentHolograms 3.0 is released, makes the hologram always face the player (in terms of if above, you will still see all the lines)." +
        "This must be used within a hologram creation section.")
@Examples({"create new holo keyed \"stacyGURLS!\":",
        "\tpage 0: \"it's going\", \"DOWWWWWN\", glowing diamond sword and an ender dragon",
        "\tpersistent: false",
        "\tstatic: true",
        "\thide from: event-player"})
@Since("2.0")
@RequiredPlugins("DecentHolograms 3.0")
public class EffStatic extends Effect {

    static {
        HoloProvider provider = HoloHandler.get().getCurrentProvider();
        if (provider.supportsStatic()) {
            Skript.registerEffect(EffStatic.class,
                    "static\\: %boolean%");
        }
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
        Skript.error("You cannot edit a hologram outside of a section using this pattern.");
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
