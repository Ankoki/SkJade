package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HologramManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Remove Line")
@Description("Removes a line from a hologram")
@Examples("remove line number 3 from the hologram with id \"testHolo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.0")
public class EffRemoveLine extends Effect {

    static {
        Skript.registerEffect(EffRemoveLine.class,
                "remove %hologramlines% from %holograms%");
    }

    private Expression<Number> number;
    private Expression<Hologram> hologram;

    @Override
    protected void execute(Event event) {
        HologramManager.removeLine(hologram.getSingle(event), number.getSingle(event).intValue());
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "remove line";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        number = (Expression<Number>) exprs[0];
        hologram = (Expression<Hologram>) exprs[1];
        return true;
    }
}
