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
@Since("1.1.0")
public class CondIsWet extends Condition {

    static {
        Skript.registerCondition(CondIsWet.class,
                "%player% is(1¦(n[']t| not)|) [standing] in water",
                "%player% is(1¦(n[']t| not)|) [standing] in [the] rain",
                "%player% is(1¦(n[']t| not)|) [standing] in a bubble column",
                "%player% is(1¦(n[']t| not)|) [standing] in water or [a] bubble column",
                "%player% is(1¦(n[']t| not)|) [standing] in [the] rain or [a] bubble column",
                "%player% is(1¦(n[']t| not)|) (wet|soaked|moist|damp|drenched|sopping|soggy|dripping)",
                "%player% is(1¦(n[']t| not)|) in lava",
                "%player% is(1¦(n[']t| not)|) [standing] in lava or water",
                "%player% is(1¦(n[']t| not)|) (in|touching) any [type of] liquid");
    }

    private Expression<Player> player;
    private int pattern;
    private boolean negate;

    @Override
    public boolean check(Event e) {
        if (player == null) return false;
        Player p = player.getSingle(e);
        if (p == null) return false;
        switch (pattern) {
            case 0:
                return negate != p.isInWater();
            case 1:
                return negate != p.isInRain();
            case 2:
                return negate != p.isInBubbleColumn();
            case 3:
                return negate != p.isInWaterOrBubbleColumn();
            case 4:
                return negate != (p.isInRain() || p.isInBubbleColumn());
            case 5:
                return negate != p.isInWaterOrRainOrBubbleColumn();
            case 6:
                return negate != p.isInLava();
            case 7:
                return negate != (p.isInWater() || p.isInLava());
            case 8:
                return negate != (p.isInWaterOrRainOrBubbleColumn() || p.isInLava());
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
        negate = parseResult.mark == 1;
        return true;
    }
}