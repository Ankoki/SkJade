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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Force Wake")
@Description("Forces a player to wake up if they are sleeping.")
@Examples("force {_p} to wake up and set spawn point")
@Since("1.0.0")
public class EffForceWake extends Effect {

    static {
        Skript.registerEffect(EffForceWake.class,
                "(force|make) %players% [to] wake[ ]up [(1¦and set spawn[[ ]point])]");
    }

    private Expression<Player> playerExpr;
    private boolean setPoint;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        setPoint = parseResult.mark == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        Arrays.stream(playerExpr.getArray(event)).forEach(player -> {
            if (player == null) return;
            player.wakeup(setPoint);
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "force " + playerExpr.toString(event, debug) + " to wake";
    }

}
