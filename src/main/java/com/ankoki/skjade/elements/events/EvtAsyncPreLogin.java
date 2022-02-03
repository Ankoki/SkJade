package com.ankoki.skjade.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Name("Async PreLogin")
@Description("Called when a player is connecting/logging on. You cannot get the player here.")
@Examples("on async player pre-login:")
@Since("1.1.0")
public class EvtAsyncPreLogin extends SimpleEvent {

    static {
        Skript.registerEvent("Async PreLogin", EvtAsyncPreLogin.class, AsyncPlayerPreLoginEvent.class,
                "[async] [player] pre( |-)login");
        EventValues.registerEventValue(AsyncPlayerPreLoginEvent.class, UUID.class, new Getter<UUID, AsyncPlayerPreLoginEvent>() {
            @Nullable
            @Override
            public UUID get(AsyncPlayerPreLoginEvent e) {
                return e.getUniqueId();
            }
        }, 0);
    }

    @Override
    public boolean check(Event event) {
        return event instanceof AsyncPlayerPreLoginEvent;
    }
}
