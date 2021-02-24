package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Sort")
@Description("Sorts a list and stores it in a seperate list.")
@Examples("sort {_list::*} in ascending order in {_sorted::*}")
@Since("insert ver")
public class EffSort extends Effect {

    /*static {
        Skript.registerEffect(EffSort.class,
                "sort %objects% [[in] (1¦descending|ascending)] [order] in %~objects%");
    }*/

    private Expression<Object> sort;
    private Expression<Variable<Object>> store;
    private boolean ascending;

    @Override
    protected void execute(Event e) {
        Bukkit.getScheduler().runTaskAsynchronously(SkJade.getInstance(), () -> {
            if (store == null || sort == null) return;
            Object[] sorted = sort.getArray(e);
            Arrays.sort(sorted);
            if (!ascending) {
                for (int i = 0; i < sorted.length / 2; i++) {
                    Object temp = sorted[i];
                    sorted[i] = sorted[sorted.length - 1 - i];
                    sorted[sorted.length - 1 - i] = temp;
                }
            }
            store.change(e, sorted, Changer.ChangeMode.SET);
        });
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "sort " + sort.toString(e, debug) + " in " + (ascending ? "ascending " : "desending ") + "in " + store.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        sort = (Expression<Object>) exprs[0];
        if (exprs[0].isSingle() || !(exprs[1] instanceof Variable) || exprs[1].isSingle()) {
            Skript.error("You cannot sort a single variable!");
        }
        store = (Expression<Variable<Object>>) exprs[1];
        ascending = parseResult.mark != 1;
        return true;
    }
}
