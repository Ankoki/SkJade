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
import org.eclipse.jdt.annotation.Nullable;

@Name("Lasagna")
@Description("Lasagna.")
@Examples("lasagna")
@Since("1.1.0")
public class EffLasagna extends Effect {

    static {
        Skript.registerEffect(EffLasagna.class, "(lasagna|an Italian dish made of stacked layers of thin flat pasta alternating with fillings and other vegetables, cheese and seasonings and spices)");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }
    
    @Override
    protected void execute(Event e) {
        if (Utils.getServerMajorVersion() >= 16) {
            Bukkit.broadcastMessage(Utils.simpleRainbow("&l&o&nlasagna", true));
        } else {
            Bukkit.broadcastMessage("lasagna");
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "lasagna";
    }
}
