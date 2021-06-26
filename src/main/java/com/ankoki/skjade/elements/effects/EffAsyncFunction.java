package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.function.EffFunctionCall;
import ch.njol.skript.lang.function.FunctionReference;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/*Don't think this will go anywhere, however im keeping it on the table.
  I really think this would be useful, ESPECIALLY on minehut who do
  not have skript-reflect, however it comes with too many risks and
  errors that could be thrown that i don't think i could handle manually.*/
public class EffAsyncFunction extends Effect {

    /*static {
        Skript.registerEffect(EffAsyncFunction.class,
                "run [[the] function] <(.+)>\\([<.*?>]\\) async");
    }*/

    private EffFunctionCall functionCall;

    @Override
    protected void execute(Event e) {
        if (functionCall == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(SkJade.getInstance(), () -> functionCall.run(e));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "run " + functionCall.toString(e, debug) + " async";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        String unparsed = parseResult.regexes.get(0).group(0) + "(" + (parseResult.regexes.size() > 1 ? parseResult.regexes.get(1).group(0) : "") + ")";
        FunctionReference<?> function = new SkriptParser(unparsed, SkriptParser.ALL_FLAGS, ParseContext.DEFAULT)
                .parseFunction((Class<?>[]) null);
        if (function == null) {
            Skript.error("This isn't a valid function, or it doesn't exist!");
            return false;
        }
        functionCall = new EffFunctionCall(function);
        return true;
    }
}
