package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import ch.njol.skript.Skript;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DHHolo implements SKJHolo {

    private final Hologram current;
    private final String key;

    public static DHHolo create(String key, Location location, Map<Integer, List<SKJHoloLine>> pages) {
        if (HoloHandler.get().getHolo(key) != null) {
            Skript.error("The given key already exists. Hologram will not be created.");
            return null;
        }
        return new DHHolo(key, location, pages);
    }

    private DHHolo(String key, Location location, Map<Integer, List<SKJHoloLine>> pages) {
        this.key = key;
        this.current = DHAPI.createHologram(key, location);
        this.setPages(pages);
        for (HologramPage page : current.getPages()) {
            page.addAction(ClickType.LEFT, new Action("SKJADE:" + key + "." + page.getIndex() + ".LEFT"));
            page.addAction(ClickType.RIGHT, new Action("SKJADE:" + key + "." + page.getIndex() + ".RIGHT"));
            page.addAction(ClickType.SHIFT_LEFT, new Action("SKJADE:" + key + "." + page.getIndex() + ".SHIFT_LEFT"));
            page.addAction(ClickType.SHIFT_RIGHT, new Action("SKJADE:" + key + "." + page.getIndex() + ".SHIFT_RIGHT"));
        }
        this.register(key);
    }

    @Override
    public @NotNull String getKey() {
        return key;
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
        int difference = current.getPages().size() - page;
        for (int i = 0; i < difference; i++)
            this.current.addPage();
        try {
            DHAPI.setHologramLine(current, page, index, line.getContent());
        } catch (IllegalArgumentException ex) {
            DHAPI.addHologramLine(current, page, line.getContent());
        }
    }

    @Override
    public void setLines(int page, List<SKJHoloLine> lines) {
        int index = 0;
        for (SKJHoloLine line : lines) {
            this.setLine(page, index, line);
            index++;
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
    public boolean canSee(Player player) {
        return current.isVisible(player);
    }

    @Override
    public void delete() {
        current.destroy();
        HoloHandler.get().deleteHolo(key);
    }

    @Override
    public boolean isDeleted() {
        // TODO determine how DecentHolograms detect if it has been deleted/destroyed.
        return false;
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
    public Map<Integer, List<SKJHoloLine>> getPages() {
        Map<Integer, List<SKJHoloLine>> map = new HashMap<>();
        int i = 0;
        for (HologramPage page : current.getPages()) {
            map.put(i, DHHoloLine.parseDHLines(page.getLines()));
            i++;
        }
        return map;
    }

    @Override
    public List<SKJHoloLine> getPage(int page) {
        return null;
    }

    @Override
    public int getPage(Player player) {
        return current.getPage(player).getIndex();
    }

    @Override
    public Location getLocation() {
        return current.getLocation();
    }

    @Override
    public Object getHologram() {
        return current;
    }

}
