package com.ankoki.skjade.hooks.holograms.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

@Name("Hologram")
@Description({"Creates a new hologram with a key and location. If the key or location are unset, the hologram will not be created.",
            "Please note the given example is using DecentHolograms."})
@Examples({"create new holo keyed \"stacyGURLS!\" at 2 blocks above player:",
            "\tpage 0: \"it's going\", \"DOWWWWWN\", glowing diamond sword and an ender dragon",
            "\tpersistent: false",
            "\tstatic: true",
            "\ton right shift click on page 2:",
            "\t\tsend \"Hey! You clicked %event-hologram's id%!\""})
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class SecKeyedHologram extends Section {

    static {
        Skript.registerSection(SecKeyedHologram.class, "create [new] holo[gram] (with key|(key|nam)ed [as]) %string% at %location%");
    }

    private SKJHoloBuilder currentBuilder;

    private Expression<String> keyExpr;
    private Expression<Location> locationExpr;
    private Trigger trigger;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        SkriptEvent event = this.getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class)) {
            Skript.error("You can't create another hologram in a hologram creation section.");
            return false;
        }
        keyExpr = (Expression<String>) exprs[0];
        locationExpr = (Expression<Location>) exprs[1];
        trigger = loadCode(sectionNode, "hologram creation", getParser().getCurrentEvents());
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        String key = keyExpr.getSingle(event);
        Location location = locationExpr.getSingle(event);
        if (key != null && location != null) {
            this.currentBuilder = new SKJHoloBuilder(key, location);
            trigger.execute(event);
            this.currentBuilder.build();
            this.currentBuilder = null;
        }
        return this.getNext();
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create new holo keyed as " + keyExpr.toString(event, debug);
    }

    /**
     * Gets the current builder.
     * @return the current builder.
     */
    public SKJHoloBuilder getCurrentBuilder() {
        return currentBuilder;
    }

    /**
     * Sets the current builder.
     * @param currentBuilder the new builder.
     */
    public void setCurrentBuilder(SKJHoloBuilder currentBuilder) {
        this.currentBuilder = currentBuilder;
    }

}
