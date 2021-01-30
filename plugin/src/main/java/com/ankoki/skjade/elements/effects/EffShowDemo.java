package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class EffShowDemo extends Effect {

    static {
        if (SkJade.isNmsEnabled()) {
            Skript.registerEffect(EffShowDemo.class,
                    "show the demo[nstration] screen to %player%");
        }
    }

    private Expression<Player> player;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        if (p == null) return;
        SkJade.getNMS().sendDemo(p);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "show the demo screen to " + player.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
