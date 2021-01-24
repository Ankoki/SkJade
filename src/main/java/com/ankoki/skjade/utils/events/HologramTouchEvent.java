package com.ankoki.skjade.utils.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.CollectableLine;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HologramTouchEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final Hologram hologram;
    private final CollectableLine hologramLine;

    public HologramTouchEvent(Player player, Hologram hologram, CollectableLine hologramLine) {
        this.player = player;
        this.hologram = hologram;
        this.hologramLine = hologramLine;
    }

    public Player getPlayer() {
        return player;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public CollectableLine getLine() {
        return hologramLine;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
