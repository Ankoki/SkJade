package com.ankoki.skjade.hooks.holograms.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.internal.HologramController;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

public class SecKeyedHologram extends Section {

    static {
        Skript.registerSection(SecKeyedHologram.class, "create new holo[gram] keyed as %string%");
    }

    private Expression<String> keyExpr;
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
        if (key == null) return item;
        trigger.execute(event);
        HologramController.Builder.getInstance().setKey(key);
        if (HologramController.Builder.getInstance().isComplete()) {
            HologramController.Builder.getInstance().buildCurrent();
            HologramController.Builder.getInstance().reset();
        } return item;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create new holo keyed as " + keyExpr.toString(event, b);
    }
}
