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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Fake Damage")
@Description("Makes a player look like they took damage.")
@Examples("make event-player take fake damage")
@Since("1.0.0")
public class EffFakeDamage extends Effect {

    static {
        if (SkJade.isNmsEnabled()) {
            Skript.registerEffect(EffFakeDamage.class,
                    "make %players% take fake damage [for %-players%]");
        }
    }

    private Expression<Player> player;
    private Expression<Player> sendTo;

    @Override
    protected void execute(Event e) {
        Player[] sending;
        if (sendTo != null) sending = sendTo.getArray(e);
        else sending = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        Player[] damagers = player.getArray(e);
        SkJade.getNmsHandler().playFakeDamage(damagers, sending);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make " + player.toString(e, debug) + " take fake damage for " + (sendTo == null ? "all players" : sendTo.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        sendTo = (Expression<Player>) exprs[1];
        return SkJade.isNmsEnabled();
    }
}
