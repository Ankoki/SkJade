package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffHideEntity extends Effect {

    static {
        Skript.registerEffect(EffHideEntity.class,
                "hide [the] [entity] %entities% (1¦from %-players%|)");
    }

    private Expression<Entity> entity;
    private Expression<Player> player;

    @Override
    protected void execute(Event e) {

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "hide " + entity.toString(e, debug) + (player == null ? "" : " from " + player.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return false;
    }
}
