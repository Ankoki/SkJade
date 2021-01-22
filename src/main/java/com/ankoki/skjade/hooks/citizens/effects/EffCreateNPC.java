package com.ankoki.skjade.hooks.citizens.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class EffCreateNPC extends Effect {

    static {
        Skript.registerEffect(EffCreateNPC.class,
                "create [[a] new] [jenkins] (citizen|npc) of %entitytype% at %location% (with [the] name|named) %string%");
    }

    private Expression<EntityType> entityType;
    private Expression<Location> location;
    private Expression<String> name;

    @Override
    protected void execute(Event event) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(entityType.getSingle(event), name.getSingle(event));
        npc.spawn(location.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create npc";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        entityType = (Expression<EntityType>) exprs[0];
        location = (Expression<Location>) exprs[1];
        name = (Expression<String>) exprs[2];
        return true;
    }
}
