package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Lasagna")
@Description("Lasagna.")
@Examples("lasagna")
@Since("1.1.0")
public class EffLasagna extends Effect {

    static {
        Skript.registerEffect(EffLasagna.class, "lasagna");
    }

    @Override
    protected void execute(Event e) {
        Bukkit.broadcastMessage(Utils.rainbow("&l&o&nlasagna", 0.3, 0.3, 0.3, 0, 2, 4, true));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "lasagna";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }
}
