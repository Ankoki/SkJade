package com.ankoki.skjade.hooks.holograms.api;

public enum ClickType {
	LEFT,
	RIGHT,
	SHIFT_LEFT,
	SHIFT_RIGHT,
	ANY;

	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
}
