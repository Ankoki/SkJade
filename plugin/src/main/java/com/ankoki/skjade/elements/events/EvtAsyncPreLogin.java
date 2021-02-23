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
import org.eclipse.jdt.annotation.Nullable;

import java.util.UUID;

@Name("Async PreLogin")
@Description("Called when a player is connecting/logging on. You cannot get the player here.")
@Examples("on async pre-login:")
@Since("insert version")
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
        //set login result.
    }

    @Override
    public boolean check(Event event) {
        return true;
    }
}
