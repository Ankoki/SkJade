package com.ankoki.skjade.hooks.holograms.api.events;

import com.ankoki.skjade.hooks.holograms.api.ClickType;
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

	/**
	 * Creates a new hologram interaction event.
	 *
	 * @param player the player who interacted.
	 * @param holo the hologram interacted with.
	 * @param page the page interacted with, -1 if pages aren't supported.
	 * @param line the line interacted with, -1 if lines aren't supported.
	 * @param type the click type.
	 */
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

	/**
	 * Gets the hologram interacted with.
	 *
	 * @return the hologram.
	 */
	public SKJHolo getHologram() {
		return holo;
	}

	/**
	 * Gets the page number, -1 if pages aren't supported.
	 *
	 * @return the page.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Gets the line number, -1 if lines aren't supported.
	 *
	 * @return the line.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Gets the click type used.
	 *
	 * @return the click type.
	 */
	public ClickType getType() {
		return type;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	/**
	 * Gets the handler list required by bukkit.
	 *
	 * @return an empty handler list.
	 */
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
