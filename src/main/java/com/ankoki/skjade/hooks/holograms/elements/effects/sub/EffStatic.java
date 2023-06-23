package com.ankoki.skjade.hooks.holograms.elements.effects.sub;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import com.ankoki.skjade.hooks.holograms.elements.sections.SecKeyedHologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

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
        if (provider.supportsStatic())
            Skript.registerEffect(EffStatic.class,
                    "static\\: %boolean%");
    }

    private SecKeyedHologram section;
    private Expression<Boolean> staticExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        SkriptEvent event = this.getParser().getCurrentSkriptEvent();
        if (!(event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class))) {
            Skript.error("You cannot set the static state outside of hologram creation using this effect.");
            return false;
        }
        section = this.getParser().getCurrentSection(SecKeyedHologram.class);
        staticExpr = (Expression<Boolean>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Boolean stat = staticExpr.getSingle(event);
        if (stat == null) return;
        SKJHoloBuilder builder = section.getCurrentBuilder();
        builder.setStatic(stat);
        section.setCurrentBuilder(builder);
    }

    /*@Override
    public List<Class<? extends Section>> getUsableSections() {
        return List.of(SecKeyedHologram.class);
    }*/

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "static: " + staticExpr.toString(event, debug);
    }

}
