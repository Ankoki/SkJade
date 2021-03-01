package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Is Wet")
@Description("Checks if a player is in any type of liquid.")
@Examples("if player is wet:")
@Since("insert version")
public class CondIsWet extends Condition {

    static {
        Skript.registerCondition(CondIsWet.class,
                "%player% is [standing] in water",
                "%player% is [standing] in [the] rain",
                "%player% is [standing] in a bubble column",
                "%player% is [standing] in water or [a] bubble column",
                "%player% is [standing] in [the] rain or [a] bubble column",
                "%player% is (wet|soaked|moist|damp|drenched|sopping|soggy|dripping)",
                "%player% is in lava",
                "%player% is [standing] in lava or water",
                "%player% is (in|touching) any liquid");
    }

    private Expression<Player> player;
    int pattern;

    @Override
    public boolean check(Event e) {
        if (player == null) return false;
        Player p = player.getSingle(e);
        if (p == null) return false;
        switch (pattern) {
            case 0:
                return p.isInWater();
            case 1:
                return p.isInRain();
            case 2:
                return p.isInBubbleColumn();
            case 3:
                return p.isInWaterOrBubbleColumn();
            case 4:
                return p.isInRain() || p.isInBubbleColumn();
            case 5:
                return p.isInWaterOrRainOrBubbleColumn();
            case 6:
                return p.isInLava();
            case 7:
                return p.isInWater() || p.isInLava();
            case 8:
                return p.isInWaterOrRainOrBubbleColumn() || p.isInLava();
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        pattern = matchedPattern;
        return true;
    }
}