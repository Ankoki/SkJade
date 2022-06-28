package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DHProvider implements HoloProvider {

    @Override
    public @NotNull String getId() {
        return "DecentHolograms";
    }

    @Override
    public boolean supportsPages() {
        return true;
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
    public @NotNull SKJHolo createHolo(String name, Location location, List<SKJHoloLine> lines) {
        return new DHHolo(name, location, lines);
    }
}
