package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Player's Spawn Point")
@Description("Gets and sets the spawn point of a player.")
@Examples("set player's spawn point to event-location")
@Since("1.3.1")
public class ExprPlayerSpawn extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprPlayerSpawn.class, Location.class, ExpressionType.SIMPLE,
                "[the] %player%[']s spawn[ point]",
                "[the] spawn [point] of %player%");
    }

    private Expression<Player> playerExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Location[] get(Event e) {
        if (playerExpr == null) return new Location[0];
        Player player = playerExpr.getSingle(e);
        if (player == null) return new Location[0];
        return new Location[]{player.getBedSpawnLocation() == null ?
                player.getWorld().getSpawnLocation() : player.getBedSpawnLocation()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return playerExpr.toString(e, debug) + "'s spawn point'";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.RESET || mode == ChangeMode.SET) {
            return CollectionUtils.array(Location.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (playerExpr == null) return;
        Player player = playerExpr.getSingle(e);
        if (player == null) return;
        if (mode == ChangeMode.RESET) {
            player.setBedSpawnLocation(null, true);
            return;
        }
        Object obj = delta[0];
        if (!(obj instanceof Location)) return;
        if (mode == ChangeMode.SET) {
            player.setBedSpawnLocation((Location) obj, true);
        }
    }
}
