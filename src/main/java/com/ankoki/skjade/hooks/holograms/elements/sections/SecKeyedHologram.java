package com.ankoki.skjade.hooks.holograms.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

@Name("Hologram")
@Description({"Creates a new hologram with a key.",
            "Please note the given example is using DecentHolograms."})
@Examples({"create new holo keyed \"stacyGURLS!\":",
            "\tlines: \"it's going\", \"DOWWWWWN\", ender dragon"})
@Since("2.0")
public class SecKeyedHologram extends Section {

    static {
        Skript.registerSection(SecKeyedHologram.class, "create [new] holo[gram] (key|nam)ed [as] %string% at %location%");
    }

    private Expression<String> keyExpr;
    private Expression<Location> locactionExpr;
    private Trigger trigger;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        SkriptEvent event = getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class)) {
            Skript.error("You can't create another hologram in a hologram creation section.");
            return false;
        }
        keyExpr = (Expression<String>) exprs[0];
        trigger = loadCode(sectionNode, "hologram creation", getParser().getCurrentEvents());

        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        TriggerItem item = walk(event, false);
        String key = keyExpr.getSingle(event);
        if (key == null) return getNext();
        trigger.execute(event);
        return item;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create new holo keyed as " + keyExpr.toString(event, b);
    }
}
