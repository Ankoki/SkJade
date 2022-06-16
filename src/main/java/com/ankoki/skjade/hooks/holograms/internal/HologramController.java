package com.ankoki.skjade.hooks.holograms.internal;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

// by drake.
public class HologramController {

    public static class Builder {
        private static final Builder instance = new Builder();
        public static Builder getInstance() {
            return instance;
        }

        private String key;
        private Location location;
        private Object[] lines;
        private Object hologram; // Will be the hologram.

        public void reset() {
            location = null;
            lines = null;
        }

        public void buildCurrent() {

        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public void setLines(Object[] lines) {
            this.lines = lines;
        }

        public boolean isComplete() {
            return location != null && lines != null && key != null;
        }

        private void parseLine(Object line) {
            if (line instanceof ItemStack item) {
                // hologram.addItemLine(item); or wtv
            } else if (line instanceof String string) {
                // hologram.addTextLine(string); or wtv
            }
        }
    }
}
