package com.ankoki.skjade.hooks.elementals.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.ankoki.elementals.events.SpellCastEvent;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Spell Cast")
@Description("This is called whenever any spell is called.")
@Examples("on any spell cast:")
@RequiredPlugins("Elementals")
@Since("1.0")
public class EvtSpellCast extends SkriptEvent {

    static {
        Skript.registerEvent("Spell Cast", EvtSpellCast.class, SpellCastEvent.class,
                "[any] spell cast");
        EventValues.registerEventValue(SpellCastEvent.class, Player.class, new Getter<Player, SpellCastEvent>() {
            @Nullable
            @Override
            public Player get(SpellCastEvent e) {
                return e.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(SpellCastEvent.class, Long.class, new Getter<Long, SpellCastEvent>() {
            @Nullable
            @Override
            public Long get(SpellCastEvent e) {
                return e.getCooldown();
            }
        }, 0);
        EventValues.registerEventValue(SpellCastEvent.class, Entity.class, new Getter<Entity, SpellCastEvent>() {
            @Nullable
            @Override
            public Entity get(SpellCastEvent e) {
                return e.getEntity();
            }
        }, 0);
        EventValues.registerEventValue(SpellCastEvent.class, Spell.class, new Getter<Spell, SpellCastEvent>() {
            @Nullable
            @Override
            public Spell get(SpellCastEvent e) {
                return e.getSpell();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] literals, int i, ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return event instanceof SpellCastEvent;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "spell cast event";
    }
}
