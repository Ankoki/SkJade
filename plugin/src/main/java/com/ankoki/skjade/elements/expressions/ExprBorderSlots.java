package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Border Slots")
@Description("Returns the border slots of an inventory.")
@Examples("set border slots of the event-inventory to glowing diamond")
@Since("1.0.0")
public class ExprBorderSlots extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprBorderSlots.class, Integer.class, ExpressionType.SIMPLE,
                "border slots of [the] [inventory] %inventory%");
    }

    private Expression<Inventory> inventory;

    @Nullable
    @Override
    protected Integer[] get(Event e) {
        List<Integer> borderSlots = new ArrayList<>();
        Inventory inv = inventory.getSingle(e);
        int nrOfRows = (inv.getSize()/9);
        for (int slot = 0; slot < inv.getSize(); slot++) {
            int s = slot%9;
            int sr = slot/nrOfRows;
            if (s == 0 || s == 1 || sr == 0 || sr == nrOfRows -1) {
                borderSlots.add(slot);
            }
        }
        return (Integer[]) borderSlots.toArray();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "border slots of " + inventory.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        inventory = (Expression<Inventory>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
            return CollectionUtils.array();
        } else if (mode == ChangeMode.SET) {
            return CollectionUtils.array(ItemType.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        Inventory inv = inventory.getSingle(e);
        if (inv == null) return;
        Integer[] slots = this.get(e);
        if (slots == null) return;
        if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
            for (int i2 = 0; i2 < inv.getSize(); i2++) {
                for (int i : slots) {
                    if (i == i2) {
                        inv.setItem(i2, null);
                    }
                }
            }
        } else if (mode == ChangeMode.SET) {
            if (delta[0] == null || !(delta[0] instanceof ItemType)) return;
            for (int i2 = 0; i2 < inv.getSize(); i2++) {
                for (int i : slots) {
                    if (i == i2) {
                        inv.setItem(i2, ((ItemType) delta[0]).getRandom());
                    }
                }
            }
        }
    }
}
