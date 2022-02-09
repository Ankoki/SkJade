package com.ankoki.skjade.hooks.holograms.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager.TouchType;
import com.gmail.filoghost.holographicdisplays.api.line.CollectableLine;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram is Interactable/Clickable/Touchable")
@Description("Checks if a hologram is interactable, clickable, or touchable")
@Examples("if event-hologram is interactable:")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class CondHasInteractionHandler extends Condition {

    static {
        Skript.registerCondition(CondHasInteractionHandler.class,
                "%hologramline% is (0¦interactable|1¦clickable|2¦touchable)");
    }

    private TouchType touchType;
    private Expression<HologramLine> hologramLine;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        switch (parseResult.mark) {
            case 0:
                touchType = TouchType.INTERACTABLE;
                break;
            case 1:
                touchType = TouchType.CLICKABLE;
                break;
            default:
                touchType = TouchType.TOUCHABLE;
        }
        hologramLine = (Expression<HologramLine>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (hologramLine == null) return false;
        HologramLine line = hologramLine.getSingle(event);
        if (line == null) return false;
        switch (touchType) {
            case INTERACTABLE:
                return ((TouchableLine) line).getTouchHandler() != null &&
                        ((CollectableLine) line).getPickupHandler() != null;
            case CLICKABLE:
                return ((TouchableLine) line).getTouchHandler() != null;
            case TOUCHABLE:
                return ((CollectableLine) line).getPickupHandler() != null;
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return hologramLine.toString(event, b) + " is " + touchType.name().toLowerCase();
    }
}
