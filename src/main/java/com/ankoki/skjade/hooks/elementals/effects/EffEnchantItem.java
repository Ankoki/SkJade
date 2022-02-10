package com.ankoki.skjade.hooks.elementals.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Enchant Item")
@Description("Add an elementals spell to an item")
@Examples("add the elementals enchant \"umbrial\" to player's tool")
@RequiredPlugins("Elementals")
@Since("1.0.0")
public class EffEnchantItem extends Effect {

    static {
        Skript.registerEffect(EffEnchantItem.class,
                "add %spell% to %itemstack%");
    }

    private Expression<Spell> spellExprs;
    private Expression<ItemStack> item;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        spellExprs = (Expression<Spell>) exprs[0];
        item = (Expression<ItemStack>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        ItemStack i = item.getSingle(event);
        Spell spell = spellExprs.getSingle(event);
        if (i == null || spell == null) return;
        if (i.getType().isBlock()) return;
        item.change(event, new ItemStack[] {new ItemManager(i).addSpell(spell).getItem()}, ChangeMode.SET);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "add " + spellExprs.toString(event, b) + " to " + item.toString(event, b);
    }
}
