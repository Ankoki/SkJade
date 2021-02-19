package com.ankoki.skjade.hooks.elementals.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.ankoki.elementals.events.ProlongedSpellCancelEvent;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Prolonged Spell Cancel")
@Description("This is called whenever any prolonged spell is cancelled.")
@Examples("on prolonged spell cancel:")
@RequiredPlugins("Elementals")
@Since("1.0.0")
public class EvtSpellCancel extends SimpleEvent {

    static {
        Skript.registerEvent("Prolonged Spell Cancel", EvtSpellCancel.class, ProlongedSpellCancelEvent.class,
                "[elementals] [prolonged] spell cancel");
        EventValues.registerEventValue(ProlongedSpellCancelEvent.class, Player.class, new Getter<Player, ProlongedSpellCancelEvent>() {
            @Nullable
            @Override
            public Player get(ProlongedSpellCancelEvent e) {
                return e.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(ProlongedSpellCancelEvent.class, Long.class, new Getter<Long, ProlongedSpellCancelEvent>() {
            @Nullable
            @Override
            public Long get(ProlongedSpellCancelEvent e) {
                return e.getCooldown();
            }
        }, 0);
        EventValues.registerEventValue(ProlongedSpellCancelEvent.class, Spell.class, new Getter<Spell, ProlongedSpellCancelEvent>() {
            @Nullable
            @Override
            public Spell get(ProlongedSpellCancelEvent e) {
                return e.getSpell();
            }
        }, 0);
    }

    @Override
    public boolean check(Event event) {
        return true;
    }
}
