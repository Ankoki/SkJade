package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Is Wet")
@Description("Checks if a player (1.1.0) or an entity (1.3.2) is in any type of liquid.")
@Examples("if player is wet:")
@RequiredPlugins("Paper for anything but is in water.")
@Since("1.1.0")
public class CondIsWet extends Condition {

    static {
        if (Skript.methodExists(Player.class, "isInRain")) {
            Skript.registerCondition(CondIsWet.class,
                    "%entity% is(1¦(n[']t| not)|) [standing] in water",
                    "%entity% is(1¦(n[']t| not)|) [standing] in [the] rain",
                    "%entity% is(1¦(n[']t| not)|) [standing] in a bubble column",
                    "%entity% is(1¦(n[']t| not)|) [standing] in water or [a] bubble column",
                    "%entity% is(1¦(n[']t| not)|) [standing] in [the] rain or [a] bubble column",
                    "%entity% is(1¦(n[']t| not)|) (wet|soaked|moist|damp|drenched|sopping|soggy|dripping)",
                    "%entity% is(1¦(n[']t| not)|) in lava",
                    "%entity% is(1¦(n[']t| not)|) [standing] in lava or water",
                    "%entity% is(1¦(n[']t| not)|) (in|touching) any [type of] liquid");
        } else {
            Skript.registerCondition(CondIsWet.class,
                    "%player% is(1¦(n[']t| not)|) [standing] in water");
        }
    }

    private Expression<Entity> entityExpr;
    private int pattern;
    private boolean negate;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entityExpr = (Expression<Entity>) exprs[0];
        pattern = matchedPattern;
        negate = parseResult.mark == 1;
        return true;
    }

    @Override
    public boolean check(Event event) {
        Entity entity = entityExpr.getSingle(event);
        if (entity == null) return false;
        return switch (pattern) {
            case 0 -> negate != entity.isInWater();
            case 1 -> negate != entity.isInRain();
            case 2 -> negate != entity.isInBubbleColumn();
            case 3 -> negate != entity.isInWaterOrBubbleColumn();
            case 4 -> negate != (entity.isInRain() || entity.isInBubbleColumn());
            case 5 -> negate != entity.isInWaterOrRainOrBubbleColumn();
            case 6 -> negate != entity.isInLava();
            case 7 -> negate != (entity.isInWater() || entity.isInLava());
            case 8 -> negate != (entity.isInWaterOrRainOrBubbleColumn() || entity.isInLava());
            default -> false;
        };
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return switch (pattern) {
            case 0 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "standing in water";
            case 1 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "in rain";
            case 2 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "in a bubble column";
            case 3 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "in water or a bubble column";
            case 4 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "wet";
            case 5 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "in lava";
            case 6 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "standing in lava or water";
            case 7 -> entityExpr.toString(e, debug) + " is" + (negate ? "n't " : " ") + "touching any type of liquid";
            default -> null;
        };
    }
}