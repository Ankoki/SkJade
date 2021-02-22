package com.ankoki.skjade.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.eclipse.jdt.annotation.Nullable;

import java.util.UUID;

@Name("Async PreLogin")
public class EvtAsyncPreLogin extends SimpleEvent {

    static {
        Skript.registerEvent("Async PreLogin", EvtAsyncPreLogin.class, AsyncPlayerPreLoginEvent.class,
                "on [async] [pre[( |-)]]login");
        EventValues.registerEventValue(AsyncPlayerPreLoginEvent.class, UUID.class, new Getter<UUID, AsyncPlayerPreLoginEvent>() {
            @Nullable
            @Override
            public UUID get(AsyncPlayerPreLoginEvent e) {
                return e.getUniqueId();
            }
        }, 0);
        //  set login result and player profile. all should be expressions.
    }

    @Override
    public boolean check(Event event) {
        return true;
    }
}
