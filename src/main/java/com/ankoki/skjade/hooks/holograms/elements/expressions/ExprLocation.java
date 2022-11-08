package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Location")
@Description("Gets/sets a hologram's location.")
@Examples("set {_location} to holo keyed as \"we dem girls\"'s location")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprLocation extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprLocation.class, Location.class, ExpressionType.SIMPLE,
                "[the] loc[ation] of %skjholo%",
                "%skjholo%'s loc[ation]");
    }

    private Expression<SKJHolo> holoExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        holoExpr = (Expression<SKJHolo>) exprs[0];
        return true;
    }

    @Override
    protected @Nullable Location[] get(Event event) {
        SKJHolo holo = holoExpr.getSingle(event);
        if (holo == null) return new Location[0];
        return new Location[]{holo.getLocation()};
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
    public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
        return mode == ChangeMode.SET ? CollectionUtils.array(Location.class) : null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (mode != ChangeMode.SET && delta.length < 1) return;
        if (delta[0] instanceof Location location) {
            SKJHolo holo = holoExpr.getSingle(e);
            if (holo == null) return;
            holo.teleport(location);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return holoExpr.toString(event, b) + "'s location";
    }
}
