package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Evaluate Effect")
@Description("Execute a Skript effect from a string.")
@Examples("evaluate \"set player's flight mode to true\"")
@Since("1.0.0")
public class EffEvaluate extends Effect {

    static {
        Skript.registerEffect(EffEvaluate.class,
                "(eval[uate]|execute) %strings%");
    }

    private Expression<String> effectExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        effectExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (effectExpr == null) return;
        Arrays.stream(effectExpr.getArray(event)).forEach(effect -> {
            TriggerItem eff = Effect.parse(effect, null);
            if (eff == null) return;
            TriggerItem.walk(eff, event);
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "evaluate " + effectExpr.toString(event, debug);
    }
}
