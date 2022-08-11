package com.ankoki.skjade.hooks.holograms.impl.decentholograms;

import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DHProvider implements HoloProvider {

    @Override
    public SKJHoloLine parseLine(Object line) {
        return new DHHoloLine(line);
    }

    @Override
    public List<SKJHoloLine> parseLines(List<Object> lines) {
        List<SKJHoloLine> parsed = new ArrayList<>();
        for (Object object : lines) parsed.add(new DHHoloLine(object));
        return parsed;
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
    public @NotNull SKJHolo createHolo(String name, Location location, Map<Integer, List<SKJHoloLine>> pages) {
        return new DHHolo(name, location, pages);
    }
}
