package com.ankoki.skjade.utils.events;

import com.ankoki.skjade.SkJade;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class RealTimeEvent extends Event {

    /**
     * Registers a repeating task to call this event every minute.
     */
    public static void register() {
        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(SkJade.getInstance(), () -> Bukkit.getPluginManager().callEvent(new RealTimeEvent(new Date())), 0L, 20 * 60L);
    }

    private static final HandlerList handlerList = new HandlerList();
    private final Date date;

    /**
     * Creates a new real time event with the given date.
     *
     * @param date the date.
     */
    public RealTimeEvent(Date date) {
        this.date = date;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Gets the date of the event.
     *
     * @return the date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Required by bukkit.
     *
     * @return an empty handler list.
     */
    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
