package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
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
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

    private Expression<Inventory> inventoryExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        inventoryExpr = (Expression<Inventory>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Integer[] get(Event event) {
        Inventory inventory = inventoryExpr.getSingle(event);
        if (inventory == null) return new Integer[0];
        return getBorderSlots(inventory);
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
        return "border slots of " + inventoryExpr.toString(e, debug);
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) return CollectionUtils.array();
        else if (mode == ChangeMode.SET) return CollectionUtils.array(ItemType.class);
        else return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        Inventory inv = inventoryExpr.getSingle(e);
        if (inv == null) return;
        Integer[] slots = this.get(e);
        if (slots == null) return;
        if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) this.fillBorder(inv, null);
        else if (mode == ChangeMode.SET) {
            if (delta[0] == null || !(delta[0] instanceof ItemType)) return;
            this.fillBorder(inv, ((ItemType) delta[0]).getRandom());
        }
    }

    private void fillBorder(Inventory inv, @Nullable ItemStack fillItem) {
        for (int i : this.getBorderSlots(inv)) inv.setItem(i, fillItem);
    }

    private Integer[] getBorderSlots(Inventory inv) {
        List<Integer> slotsList = new ArrayList<>();
        InventoryType type = inv.getType();
        int rows = 0;
        if (type == InventoryType.CHEST ||
            type == InventoryType.ENDER_CHEST ||
            type == InventoryType.SHULKER_BOX ||
            type == InventoryType.BARREL) rows = inv.getSize()/9;
        if (type == InventoryType.DISPENSER ||
            type == InventoryType.DROPPER) rows = 3;
        if (type == InventoryType.HOPPER) return new Integer[]{0, 4};
        if (rows == 0) return new Integer[0];
        int slots = inv.getSize();
        int slotsPerRow = slots/rows;
        for (int i = 1; i <= rows; i++) {
            slotsList.add(slotsPerRow * (i - 1));
            slotsList.add((slotsPerRow * (i - 1)) + (slotsPerRow - 1));
        }
        for (int i = 1; i <= (slotsPerRow - 2); i++) slotsList.add(i);
        for (int i = slots - 2; i <= ((rows - 1) * slotsPerRow) + 1; i++) slotsList.add(i);
        for (int i = (inv.getSize() - 1); i >= (inv.getSize() - slotsPerRow); i--) slotsList.add(i);
        return slotsList.toArray(new Integer[0]);
    }

}
