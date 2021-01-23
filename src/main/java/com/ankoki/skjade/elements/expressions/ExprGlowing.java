package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ExprGlowing extends SimplePropertyExpression<ItemStack, ItemStack> {

    static {
        Skript.registerExpression(ExprGlowing.class, ItemStack.class, ExpressionType.PROPERTY,
                "glowing %itemstack%");
    }

    @Override
    protected String getPropertyName() {
        return "glowing";
    }

    @Override
    public ItemStack convert(ItemStack itemStack) {
        return toShiny(itemStack);
    }

    @Override
    public Class getReturnType() {
        return ItemStack.class;
    }

    private ItemStack toShiny(ItemStack itemStack) {
        if (itemStack.getType() == Material.BOW) {
            itemStack.addUnsafeEnchantment(Enchantment.RIPTIDE, 1);
        } else {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        }
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return itemStack;
    }
}
