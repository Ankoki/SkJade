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
import com.ankoki.skjade.elements.lasers.Laser;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Attach Entity to Laser")
@Description("Attaches a living entity to the end of a laser.")
@Examples("attach event-mob to end of {_laser}")
@Since("1.4")
public class EffAttachEndEntity extends Effect {

    static {
        if (Laser.isEnabled())
            Skript.registerEffect(EffAttachEndEntity.class,
                "attach %livingentity% to [the] end of %lasers%");
    }

    private Expression<LivingEntity> entityExpr;
    private Expression<Laser> laserExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult) {
        entityExpr = (Expression<LivingEntity>) expressions[0];
        laserExpr = (Expression<Laser>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        LivingEntity entity = entityExpr.getSingle(event);
        Laser[] lasers = laserExpr.getArray(event);
        if (entity == null || lasers.length < 1) return;
        try {
            for (Laser laser : lasers) {
                if (entity.getWorld() != laser.getStart().getWorld()) continue;
                ((Laser.GuardianLaser) laser).attachEndEntity(entity);
            }
        } catch (ReflectiveOperationException ex) {}
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "attach " + entityExpr.toString(event, debug) + " to end of " + laserExpr.toString(event, debug);
    }

}
