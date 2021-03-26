package com.ankoki.skjade.elements.lasers.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.elements.utils.LaserManager;
import com.ankoki.skjade.utils.Laser;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Create Laser/Guardian Beam")
@Description("Creates a laser/guardian beam. Does NOT show it. For an infinite beam, use -1 seconds.")
@Examples("create a new laser from player to player's target block for 10 seconds with the id \"kachow\"")
@Since("insert version")
public class EffCreateLaser extends Effect {

    static {
        Skript.registerEffect(EffCreateLaser.class,
                "create [a] [new] (la(s|z)er [beam]|guardian beam) from %location% to %location% for %number% second[s] with [the] id %string%");
    }

    private Expression<Location> locationOne, locationTwo;
    private Expression<Number> time;
    private Expression<String> key;

    @Override
    protected void execute(Event e) {
        if (locationOne == null || locationTwo == null || time == null || key == null) return;
        Location loc1 = locationOne.getSingle(e);
        Location loc2 = locationTwo.getSingle(e);
        Number sec = time.getSingle(e);
        String id = key.getSingle(e);
        if (loc1 == null || loc2 == null || sec == null || id == null || id.isEmpty()) return;
        int seconds = sec.intValue();
        try {
            Laser laser = new Laser(loc1, loc2, seconds, 100);
            LaserManager.createLaser(id, laser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create a new laser from " + locationOne.toString(e, debug) + " to " + locationTwo.toString(e, debug) +
                " for " + time.toString(e, debug) + " seconds with the id " + key.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        locationOne = (Expression<Location>) exprs[0];
        locationTwo = (Expression<Location>) exprs[1];
        time = (Expression<Number>) exprs[2];
        key = (Expression<String>) exprs[3];
        return true;
    }
}
