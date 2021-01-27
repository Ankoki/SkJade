package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Pickup Delay")
@Description("Get or change the delay before an item will be available to be picked up by players.")
@Examples("set pickup delay of {_itemEntity} to 5 seconds")
@Since("1.0.0")
public class ExprPickupDelay extends PropertyExpression<Entity, Timespan> {

    static {
        Skript.registerExpression(ExprPickupDelay.class, Timespan.class, ExpressionType.COMBINED,
                "pick[ ]up (time|delay)");
    }

    @Override
    protected Timespan[] get(Event event, Entity[] entities) {
        Entity ent = entities[0];
        if (!(ent instanceof Item)) return null;
        return new Timespan[]{new Timespan(((Item) ent).getPickupDelay() * 50L)};
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "pickup delay";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return new Class[]{Timespan.class};
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (delta == null || delta[0] == null) return;
        if (mode != ChangeMode.SET || !(delta[0] instanceof Timespan)) return;
        Timespan time = (Timespan) delta[0];
        Arrays.stream(getExpr().getArray(e)).forEach(entity -> {
            if (!(entity instanceof Item)) return;
            ((Item) entity).setPickupDelay((int) time.getTicks_i());
        });
    }
}
