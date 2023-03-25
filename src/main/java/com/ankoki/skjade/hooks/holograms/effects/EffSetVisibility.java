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

@Name("Set Visibility")
@Description("Sets the visibility of a hologram to show or hide certain players.")
@Examples({"show hologram with id \"%arg-1%-hologram\" to arg-1",
        "hide the hologram with id \"%player%-hologram\" from player"})
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0")
public class EffSetVisibility extends Effect {

    static {
        Skript.registerEffect(EffSetVisibility.class,
                "(1¦show|2¦hide) %holograms% (to|from) %players%");
    }

    private Expression<Hologram> holoExpr;
    private Expression<Player> playerExpr;
    private boolean show;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        holoExpr = (Expression<Hologram>) exprs[0];
        playerExpr = (Expression<Player>) exprs[1];
        show = parseResult.mark == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        Hologram[] holograms = holoExpr.getArray(event);
        Player[] players = playerExpr.getArray(event);
        for (Hologram hologram : holograms) {
            for (Player player : players) {
                if (show)
                    hologram.getVisibilityManager().showTo(player);
                else
                    hologram.getVisibilityManager().hideTo(player);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return (show ? "show " : "hide ") + holoExpr.toString(event, b) + (show ? " to " : " from ") + playerExpr.toString(event, b);
    }
}
