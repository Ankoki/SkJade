package com.ankoki.skjade.hooks.elementals.condition;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Can Cast Spell")
@Description("Checks if a player can cast the spell specified")
@Examples("if player can cast the spell \"regrowth\":")
@RequiredPlugins("Elementals")
@Since("1.0")
public class CondCanCastSpell extends Condition {

    static {
        Skript.registerCondition(CondCanCastSpell.class,
                "%player% can cast %spell%");
    }

    private Expression<Player> player;
    private Expression<String> spell;

    @Override
    public boolean check(Event event) {
        return player.getSingle(event).hasPermission("elementals.spell." + spell.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "can cast spell";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        spell = (Expression<String>) exprs[1];
        return true;
    }
}
