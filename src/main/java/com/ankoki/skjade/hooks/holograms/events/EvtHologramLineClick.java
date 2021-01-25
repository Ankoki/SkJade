package com.ankoki.skjade.hooks.holograms.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.ankoki.skjade.utils.events.HologramClickEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Line Click")
@Description("Called when an interactable hologram line is clicked.")
@Examples("on holo line click:")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EvtHologramLineClick extends SkriptEvent {

    static {
        Skript.registerEvent("Hologram Line Click", EvtHologramLineClick.class, HologramClickEvent.class,
                "[(holographic displays|hd)] holo[gram] [line] click");
        EventValues.registerEventValue(HologramClickEvent.class, Player.class, new Getter<Player, HologramClickEvent>() {
            @Nullable
            @Override
            public Player get(HologramClickEvent e) {
                return e.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(HologramClickEvent.class, Hologram.class, new Getter<Hologram, HologramClickEvent>() {
            @Nullable
            @Override
            public Hologram get(HologramClickEvent e) {
                return e.getHologram();
            }
        }, 0);
        EventValues.registerEventValue(HologramClickEvent.class, HologramLine.class, new Getter<HologramLine, HologramClickEvent>() {
            @Nullable
            @Override
            public HologramLine get(HologramClickEvent e) {
                return e.getLine();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return event instanceof HologramClickEvent;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "hologram line click";
    }
}
