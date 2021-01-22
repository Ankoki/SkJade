package com.ankoki.skjade.hooks.citizens.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class CondIsCitizen extends Condition {

    static {
        Skript.registerCondition(CondIsCitizen.class,
                "%entity% is [a] [jenkins] (citizen|npc)");
    }

    private Expression<LivingEntity> entity;

    @Override
    public boolean check(Event event) {
        try {
            return entity.getSingle(event).hasMetadata("NPC");
        } catch (Exception ex) { return false; }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "is citizen";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        entity = (Expression<LivingEntity>) exprs[0];
        return true;
    }
}
