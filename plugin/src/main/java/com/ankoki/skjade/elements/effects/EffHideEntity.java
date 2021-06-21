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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Hide Entity")
@Description("Hides an entity from a player or all players.")
@Examples("hide player's target entity for all players")
@Since("1.3.0")
public class EffHideEntity extends Effect {

    static {
        if (SkJade.getInstance().isNmsEnabled()) {
            Skript.registerEffect(EffHideEntity.class,
                    "[skjade] (hide|destory|send [a] destroy packet for) [the] [entity] %entities% (1¦(from|for) %-players%|)");
        }
    }

    private Expression<Entity> entity;
    private Expression<Player> player;

    @Override
    protected void execute(Event e) {
        if (entity == null) return;
        Entity[] entities = entity.getArray(e);
        if (player == null) {
            SkJade.getInstance().getNmsHandler().hideEntity(Bukkit.getOnlinePlayers().toArray(new Player[0]), entities);
            return;
        }
        Player[] players = player.getArray(e);
        SkJade.getInstance().getNmsHandler().hideEntity(players, entities);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "hide " + entity.toString(e, debug) + (player == null ? "" : " from " + player.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entity = (Expression<Entity>) exprs[0];
        player = parseResult.mark == 1 ? (Expression<Player>) exprs[1] : null;
        return true;
    }
}
