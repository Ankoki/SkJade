package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Register Placeholder")
@Description("Registers a placeholder for HolographicDisplays only.")
@Examples("register placeholder \"onlinePlayers\" to refresh every 2 seconds to run funcThatReturnsString()")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EffRegisterPlaceholder extends Effect {

    //Figure out how to take in functions in a pattern to execute them, they have to return a string.
    //static {
    //    Skript.registerEffect(EffRegisterPlaceholder.class,
    //            "register [[a] holo[graphic[ displays]]] placeholder %string% ([to have]|with) refresh rate %number% to run %function%");
    //}

    private Expression<String> text;
    private Expression<Number> refreshRate;
    private Expression<?> function;

    @Override
    protected void execute(Event event) {
        HologramsAPI.registerPlaceholder(SkJade.getInstance(),
                text.getSingle(event),
                refreshRate.getSingle(event).doubleValue(), () -> {
                    //execute function
                    return "";
                });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "register holographic displays placeholder";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        refreshRate = (Expression<Number>) exprs[1];
        function = exprs[2];
        return true;
    }
}
