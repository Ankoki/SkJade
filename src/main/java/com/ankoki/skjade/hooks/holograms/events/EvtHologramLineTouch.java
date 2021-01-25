package com.ankoki.skjade.hooks.holograms.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.ankoki.skjade.utils.events.HologramTouchEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Line Touch")
@Description("Called when an interactable hologram line is touched.")
@Examples("on holo line touch:")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EvtHologramLineTouch extends SkriptEvent {

    static {
        Skript.registerEvent("Hologram Line Touch", EvtHologramLineTouch.class, HologramTouchEvent.class,
                "[(holographic[ ]displays|hd)] holo[gram] [line] touch");
        EventValues.registerEventValue(HologramTouchEvent.class, Player.class, new Getter<Player, HologramTouchEvent>() {
            @Nullable
            @Override
            public Player get(HologramTouchEvent e) {
                return e.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(HologramTouchEvent.class, Hologram.class, new Getter<Hologram, HologramTouchEvent>() {
            @Nullable
            @Override
            public Hologram get(HologramTouchEvent e) {
                return e.getHologram();
            }
        }, 0);
        EventValues.registerEventValue(HologramTouchEvent.class, HologramLine.class, new Getter<HologramLine, HologramTouchEvent>() {
            @Nullable
            @Override
            public HologramLine get(HologramTouchEvent e) {
                return e.getLine();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] literals, int i, ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return event instanceof HologramTouchEvent;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "hologram line touch";
    }
}
