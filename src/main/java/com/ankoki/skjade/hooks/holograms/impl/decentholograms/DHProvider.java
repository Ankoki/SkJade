package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.*;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DHProvider implements HoloProvider, Listener {

    private static final DHProvider INSTANCE = new DHProvider();

    public static DHProvider get() {
        return INSTANCE;
    }

    @Override
    public SKJHoloLine parseLine(Object line) {
        return new DHHoloLine(line);
    }

    @Override
    public List<SKJHoloLine> parseLines(List<Object> lines) {
        List<SKJHoloLine> parsed = new ArrayList<>();
        for (Object object : lines)
            parsed.add(new DHHoloLine(object));
        return parsed;
    }

    @Override
    public ClickType parseClickType(Object click) {
        return ClickType.valueOf(((Enum<?>) click).name());
    }

    @Override
    public @NotNull String getId() {
        return "DecentHolograms";
    }

    @Override
    public boolean supportsPages() {
        return true;
    }

    @Override
    public boolean supportsOnClick(boolean singleLine) {
        return !singleLine;
    }

    @Override
    public boolean supportsOnTouch() {
        return false;
    }

    @Override
    public boolean supportsPersistence() {
        return true;
    }

    @Override
    public boolean supportsStatic() {
        return true;
    }

    @Override
    public boolean supportsPerPlayer() {
        return true;
    }

    @Override
    public @Nullable SKJHolo createHolo(String name, Location location, Map<Integer, List<SKJHoloLine>> pages) {
        return DHHolo.create(name, location, pages);
    }

    @Override
    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, SkJade.getInstance());
        new ActionType("SKJADE") {
            public boolean execute(Player player, String... args) {
                if (args.length != 1)
                    return false;
                // args = "holoname.pageindex.clicktype"
                args = args[0].split("\\.");
                Event call = new HologramInteractEvent(player, HoloHandler.get().getHolo(args[0]), Integer.getInteger(args[1]), -1, parseClickType(args[2]));
                Bukkit.getPluginManager().callEvent(call);
                return true;
            }
        };
    }

    @Override
    public SKJHolo getHologramFrom(Object object) {
        if (object instanceof Hologram hologram) {
            for (SKJHolo holo : HoloHandler.get().getHolograms()) {
                if (holo.getHologram() == hologram) return holo;
            }
        } return null;
    }

    // @EventHandler
    private void onHologramClick(HologramClickEvent event) {
        final SKJHolo holo = this.getHologramFrom(event.getHologram());
        if (holo == null) return;
        Event call = new HologramInteractEvent(event.getPlayer(), holo, event.getPage().getIndex(), -1, this.parseClickType(event.getClick()));
        Bukkit.getPluginManager().callEvent(call);
    }
}
