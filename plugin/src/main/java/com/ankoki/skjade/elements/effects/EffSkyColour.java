package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

//thank you pesekjan c:
@Name("Change the Sky Colour")
@Description("Changes the sky colour for players. The maximum number is 200 to make sure clients aren't crashed.")
@Examples("change the sky colour to 5 for all players")
@Since("1.1.0")
@RequiredPlugins("1.16+")
public class EffSkyColour extends Effect {

    static {
        Skript.registerEffect(EffSkyColour.class,
                "change [the] sky colo[u]r to %number% for %players%");
    }

    private Expression<Number> numbers;
    private Expression<Player> players;

    @Override
    protected void execute(Event e) {
        if (numbers == null || players == null) return;
        Number num = numbers.getSingle(e);
        if (num == null) return;
        int i = num.intValue();
        Player[] p = players.getArray(e);
        if (p.length <= 0) return;
        i = Math.min(i, 200);
        SkJade.getNmsHandler().changeSkyColour(i, p);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "change the sky colour to " + numbers.toString(e, debug) + " for " + players.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        numbers = (Expression<Number>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }
}
