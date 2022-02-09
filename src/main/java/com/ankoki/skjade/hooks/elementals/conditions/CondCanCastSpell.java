package com.ankoki.skjade.hooks.elementals.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Can Cast Spell")
@Description("Checks if a player can cast the spell specified")
@Examples("if player can cast the spell \"regrowth\":")
@RequiredPlugins("Elementals")
@Since("1.0.0")
public class CondCanCastSpell extends Condition {

    static {
        Skript.registerCondition(CondCanCastSpell.class,
                "%player% can cast %spell%");
    }

    private Expression<Player> player;
    private Expression<Spell> spell;

    @Override
    public boolean check(Event event) {
        if (player == null || spell == null) return false;
        Player p = player.getSingle(event);
        Spell s = spell.getSingle(event);
        if (p == null || s == null) return false;
        return p.hasPermission("elementals.spell." + s.getSpellName());
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return player.toString(event, b) + " can cast " + spell.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        spell = (Expression<Spell>) exprs[1];
        return true;
    }
}
