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

    private Expression<Player> players;
    private boolean setPoint;

    @Override
    protected void execute(Event event) {
        if (players == null) return;
        Arrays.stream(players.getArray(event)).forEach(p -> {
            if (p == null) return;
            p.wakeup(setPoint);
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "force " + players.toString(event, b) + " to wake";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        setPoint = parseResult.mark == 1;
        return true;
    }
}
