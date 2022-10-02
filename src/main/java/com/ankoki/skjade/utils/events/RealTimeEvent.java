package com.ankoki.skjade.utils.events;

import com.ankoki.skjade.SkJade;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class RealTimeEvent extends Event {

    public static void register() {
        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(SkJade.getInstance(), () -> Bukkit.getPluginManager().callEvent(new RealTimeEvent(new Date())), 0L, 20 * 60L);
    }

    private static final HandlerList handlerList = new HandlerList();
    private final Date date;

    public RealTimeEvent(Date date) {
        this.date = date;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Date getDate() {
        return date;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
