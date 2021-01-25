package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.function.EffFunctionCall;
import ch.njol.skript.lang.function.Function;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

/*@Name("Register Placeholder")
@Description("Registers a placeholder for HolographicDisplays only.")
@Examples("register placeholder \"onlinePlayers\" to run funcThatReturnsString()")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")*/
public class EffRegisterPlaceholder extends Effect {

    //Figure out how to take in functions in a pattern to execute them, they have to return a string.
    /*static {
    //    Skript.registerEffect(EffRegisterPlaceholder.class,
    //            "register [[a] holo[graphic[ displays]]] placeholder %string% to run [the] function <(.+)>\\([<.*?>]\\)");
    }*/

    private Expression<String> text;
    //From what ive found you need to use regex to get a function.
    private EffFunctionCall function;

    @Override
    protected void execute(Event event) {
        HologramsAPI.registerPlaceholder(SkJade.getInstance(),
                text.getSingle(event),
                2, () -> {
                    return "hi";
                });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "register holographic displays placeholder";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        /*if (exprs[1].getReturnType() != String.class) {
            Skript.error("You need to use a function which returns a string!");
            return false;
        }*/
        function = EffFunctionCall.parse(String.valueOf(parseResult.regexes.get(0)));
        return true;
    }
}
