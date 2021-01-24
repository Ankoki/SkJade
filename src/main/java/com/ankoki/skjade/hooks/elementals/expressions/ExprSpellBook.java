package com.ankoki.skjade.hooks.elementals.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.managers.BookManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Spell Book")
@Description("An elemental spell book with the spell name")
@Examples("give player a elementals spellbook with the spell \"dash\"")
@Since("1.0")
public class ExprSpellBook extends SimpleExpression<ItemType> {

    static {
        Skript.registerExpression(ExprSpellBook.class, ItemType.class, ExpressionType.PROPERTY,
                "[a[n]] [elementals] spell[[ ]book] with [[the] spell] %string%");
    }

    private Expression<String> string;

    @Nullable
    @Override
    protected ItemType[] get(Event event) {
        Spell spell = Utils.getSpellFromName(string.getSingle(event));
        if (spell == null) return null;
        ItemStack item = new BookManager(spell).getBook();
        return new ItemType[]{new ItemType(item)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "elementals spell book";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        string = (Expression<String>) exprs[0];
        return true;
    }
}
