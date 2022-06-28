package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import ch.njol.skript.Skript;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;

import java.util.List;

public class DHHolo implements SKJHolo {

    private final Hologram current;

    public DHHolo(String key, Location location, List<SKJHoloLine> lines) {
        this.current = DHAPI.createHologram(key, location);
        this.setLines(lines);
        if (!this.register(key)) Skript.error("The given key already exists.");
    }

    @Override
    public void teleport(Location location) {
        DHAPI.moveHologram(current, location);
    }

    @Override
    public void appendLine(SKJHoloLine line) {
        SKJHoloLine.Type type = SKJHoloLine.Type.getType(line);
        switch (type) {
            case TEXT -> DHAPI.addHologramLine(current, line.getText());
            case MATERIAL -> DHAPI.addHologramLine(current, line.getMaterial());
            case ITEM -> DHAPI.addHologramLine(current, line.getItem());
            case ENTITY -> DHAPI.addHologramLine(current, line.getContent());
            case EMPTY -> DHAPI.addHologramLine(current, "§f");
        }
    }

    @Override
    public void setLine(int index, SKJHoloLine line) {
        SKJHoloLine.Type type = SKJHoloLine.Type.getType(line);
        switch (type) {
            case TEXT -> DHAPI.setHologramLine(current, index, line.getText());
            case MATERIAL -> DHAPI.setHologramLine(current, index, line.getMaterial());
            case ITEM -> DHAPI.setHologramLine(current, index, line.getItem());
            case ENTITY -> DHAPI.addHologramLine(current, index, line.getContent());
            case EMPTY -> DHAPI.setHologramLine(current, index, "§f");
        }
    }

    @Override
    public void setLines(List<SKJHoloLine> lines) {
        int index = 0;
        for (SKJHoloLine line : lines) {
            try {
                SKJHoloLine.Type type = SKJHoloLine.Type.getType(line);
                switch (type) {
                    case TEXT -> DHAPI.setHologramLine(current, index, line.getText());
                    case MATERIAL -> DHAPI.setHologramLine(current, index, line.getMaterial());
                    case ITEM -> DHAPI.setHologramLine(current, index, line.getItem());
                    case ENTITY -> DHAPI.setHologramLine(current, index, line.getContent());
                    case EMPTY -> DHAPI.setHologramLine(current, index, "§f");
                }
                index++;
            } catch (IllegalArgumentException ex) {
                break;
            }
        }
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

    @Override
    public void destroy() {
        current.destroy();
    }
}
