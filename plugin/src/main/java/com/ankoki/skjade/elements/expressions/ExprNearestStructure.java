package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Nearest Structure")
@Description({"Find the closest nearby structure of a given structure type. Will not be set if not found. " +
        "Finding unexplored structures can, and will, block if the world is looking in chunks that gave not generated yet. " +
        "This can lead to the world temporarily freezing while locating an unexplored structure.",
        "The radius is not a rigid square radius. Each structure may alter how many chunks to check for each iteration. " +
        "Do not assume that only a radius x radius chunk area will be checked. For example, a woodland mansion can " +
                "potentially check up to 20,000 blocks away (or more) regardless of the radius used.",
        "This will not load or generate chunks. This can also lead to instances where the server can hang if you are only " +
                "looking for unexplored structures. This is because it will keep looking further and further out in order to find the structure."})
@Examples("teleport player to the closest village within radius 10 around player")
@Since("1.3.0")
public class ExprNearestStructure extends SimpleExpression<Location> {

    static {
        if (Utils.getServerMajorVersion() > 12) {
            Skript.registerExpression(ExprNearestStructure.class, Location.class, ExpressionType.SIMPLE,
                    "[the] (nearest|closest) (1¦(not |un)(explored|discovered)|) [structure [of]] %structure% (in|within) [a] radius [of] %number% (around|at|from|of) %location%");
        }
    }

    //It seems as though it will always include unexplored anyway.
    //private boolean unexplored;
    private Expression<StructureType> structureTypeExpr;
    private Expression<Number> radiusExpr;
    private Expression<Location> centerExpr;

    @Nullable
    @Override
    protected Location[] get(Event e) {
        if (structureTypeExpr == null || radiusExpr == null || centerExpr == null) return new Location[0];
        StructureType structureType = structureTypeExpr.getSingle(e);
        Number number = radiusExpr.getSingle(e);
        Location center = centerExpr.getSingle(e);
        if (structureType == null || number == null || center == null) return new Location[0];
        int radius = number.intValue();
        return new Location[]{center.getWorld().locateNearestStructure(center, structureType, radius, /*unexplored*/true)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "closest structure of " + structureTypeExpr.toString(e, debug) + " in radius " +
                radiusExpr.toString(e, debug) + " from " + centerExpr.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        //unexplored = parseResult.mark == 1;
        structureTypeExpr = (Expression<StructureType>) exprs[0];
        radiusExpr = (Expression<Number>) exprs[1];
        centerExpr = (Expression<Location>) exprs[2];
        return true;
    }
}
