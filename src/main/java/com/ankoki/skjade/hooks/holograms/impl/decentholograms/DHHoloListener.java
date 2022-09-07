package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import eu.decentsoftware.holograms.event.HologramClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DHHoloListener implements Listener {

	private static final DHHoloListener instance = new DHHoloListener();

	public static DHHoloListener getInstance() {
		return instance;
	}

	@EventHandler
	private void onHologramClick(HologramClickEvent event) {

	}
}
