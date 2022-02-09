package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Reset Visibility")
@Description("Resets the visibility of a hologram to its default visibility.")
@Examples({"reset visibility of the hologram with id \"%player%-hologram\" for all players",
            "reset the visibility of the hologram with id \"%player%-hologram\" for player"})
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class EffResetVisibility extends Effect {

    static {
        Skript.registerEffect(EffResetVisibility.class,
                "reset [the] visibility of %holograms% [(for all players|1¦for [the] [player] %-player%)]");
    }

    private Expression<Hologram> holo;
    private Expression<Player> player;
    private boolean specP;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        specP = parseResult.mark == 1;
        holo = (Expression<Hologram>) exprs[0];
        player = specP ? (Expression<Player>) exprs[1] : null;
        return true;
    }

    @Override
    protected void execute(Event event) {
        Hologram hologram = holo.getSingle(event);
        if (hologram == null) return;
        if (specP) {
            if (player.getSingle(event) == null) return;
            hologram.getVisibilityManager().resetVisibility(player.getSingle(event));
            return;
        }
        hologram.getVisibilityManager().resetVisibilityAll();
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "reset visibility of " + (specP ? "all players" : "player " + player.getSingle(event));
    }
}
