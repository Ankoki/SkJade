package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

//thank you pesekjan c:
@Name("Force Rain")
@Description("Make it start/stop raining for players.")
@Examples("make the rain stop for {queue::*}")
@Since("1.1.0")
public class EffForceRain extends Effect {

    static {
        Skript.registerEffect(EffForceRain.class,
                "((1¦force [it] to rain|force it to stop raining)|make it (1¦|stop) rain[ing]) for %players%");
    }

    private Expression<Player> players;
    private boolean rain;

    @Override
    protected void execute(Event e) {
        if (players == null) return;
        Player[] player = players.getArray(e);
        SkJade.getNmsHandler().setRaining(player, rain);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make it " + (rain ? "rain " : "stop raining ") + "for " + players.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        rain = parseResult.mark == 1;
        return true;
    }
}
