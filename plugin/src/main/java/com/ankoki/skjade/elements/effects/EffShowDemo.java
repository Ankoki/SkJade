package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Version;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Show Demo")
@Description("Shows the minecraft demonstration screen to a player.")
@Examples("show demo screen to event-player")
@Since("1.0.0")
public class EffShowDemo extends Effect {

    static {
        if (SkJade.isNmsEnabled()) {
            Skript.registerEffect(EffShowDemo.class,
                    "show [the] demo[nstration] screen to %player%");
        }
    }

    private Expression<Player> player;

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        if (p == null) return;
        SkJade.getNmsHandler().sendDemo(p);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "show the demo screen to " + player.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return SkJade.isNmsEnabled() && new Version(String.valueOf(Skript.getMinecraftVersion())).isLargerThan(new Version("1.13.2"));
    }
}
