package com.ankoki.skjade.hooks.holograms.api.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.ankoki.skjade.hooks.holograms.api.ClickType;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class HologramInteractEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	private final SKJHolo holo;
	private final int page;
	private final int line;
	private final ClickType type;
	private boolean cancelled;

	public HologramInteractEvent(Player player,
								 SKJHolo holo,
								 int page,
								 int line,
								 ClickType type) {
		super(player);
		this.holo = holo;
		this.page = page;
		this.line = line;
		this.type = type;
	}

	public SKJHolo getHologram() {
		return holo;
	}

	public int getPage() {
		return page;
	}

	public int getLine() {
		return line;
	}

	public ClickType getType() {
		return type;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public static @NotNull HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		cancelled = b;
	}
}
