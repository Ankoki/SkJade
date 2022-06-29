package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import ch.njol.skript.Skript;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class DHHolo implements SKJHolo {

    private final Hologram current;

    public DHHolo(String key, Location location, Map<Integer, List<SKJHoloLine>> pages) {
        this.current = DHAPI.createHologram(key, location);
        this.setPages(pages);
        if (!this.register(key)) Skript.error("The given key already exists.");
    }

    @Override
    public void teleport(Location location) {
        DHAPI.moveHologram(current, location);
    }

    @Override
    public void appendLine(int page, SKJHoloLine line) {
        DHAPI.addHologramLine(current, page, line.getContent());
    }

    @Override
    public void setLine(int page, int index, SKJHoloLine line) {
        DHAPI.setHologramLine(current, page, index, line.getContent());
    }

    @Override
    public void setLines(int page, List<SKJHoloLine> lines) {
        int index = 0;
        for (SKJHoloLine line : lines) {
            try {
                DHAPI.setHologramLine(current, page, index, line.getContent());
                index++;
            } catch (IllegalArgumentException ex) {
                break;
            }
        }
    }

    @Override
    public void setPages(Map<Integer, List<SKJHoloLine>> pages) {
        for (Map.Entry<Integer, List<SKJHoloLine>> entry : pages.entrySet()) {
            this.setLines(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void showTo(int page, Player[] players) {
        for (Player player : players) current.show(player, page);
    }

    @Override
    public void hideFrom(Player[] players) {
        for (Player player : players) current.hide(player);
    }

    @Override
    public void destroy() {
        current.destroy();
    }

    @Override
    public boolean isPersistent() {
        return current.isSaveToFile();
    }

    @Override
    public void setPersistent(boolean persistent) {
        current.setSaveToFile(persistent);
    }

    @Override
    public boolean isStatic() {
        return !current.isAlwaysFacePlayer();
    }

    @Override
    public void setStatic(boolean stat) {
        current.setAlwaysFacePlayer(!stat);
    }
}
