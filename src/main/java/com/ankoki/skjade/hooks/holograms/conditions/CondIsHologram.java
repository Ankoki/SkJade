package com.ankoki.skjade.hooks.holograms.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Is A Hologram")
@Description("Checks if an entity is a hologram")
@Examples("if event-entity is a hd hologram:")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class CondIsHologram extends Condition {

    static {
        Skript.registerCondition(CondIsHologram.class,
                "%entity% is a [(hd|holographic displays)] hologram");
    }

    private Expression<Entity> entity;

    @Override
    public boolean check(Event event) {
        return HologramsAPI.isHologramEntity(entity.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "is a hologram";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        entity = (Expression<Entity>) exprs[0];
        return true;
    }
}
