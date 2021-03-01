package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Player;

@Name("Is Wet")
@Description("Checks if a player is wet.")
@Examples("if player is wet:")
@Since("insert version")
public class CondIsWet extends PropertyCondition<Player> {

    static {
        register(CondIsWet.class, "([standing] in water|wet)", "players");
    }

    @Override
    public boolean check(Player player) {
        return player.isInWater();
    }

    @Override
    protected String getPropertyName() {
        return "wet";
    }
}