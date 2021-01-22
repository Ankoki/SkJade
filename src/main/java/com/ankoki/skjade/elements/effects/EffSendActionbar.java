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
import com.ankoki.skjade.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Send Actionbar")
@Description("Sends an action bar to a player. The time shown cannot be changed.")
@Examples("send action bar with text \"test action bar\" to player")
@Since("1.0")
public class EffSendActionbar extends Effect {

    static {
        Skript.registerEffect(EffSendActionbar.class,
                "send action bar [with [text]] %string% to %player%");
    }

    private Expression<String> message;
    private Expression<Player> player;

    @Override
    protected void execute(Event event) {
        Utils.sendActionbar(player.getSingle(event), message.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "action bar";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        message = (Expression<String>) exprs[0];
        player = (Expression<Player>) exprs[1];
        return true;
    }
}
