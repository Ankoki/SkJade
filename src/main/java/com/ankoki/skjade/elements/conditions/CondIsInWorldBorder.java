package com.ankoki.skjade.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.Location;

@Name("Within World Border")
@Description("Checks if the given location is within the world border.")
@Examples({"if {_futureLocation} is not inside the world border:",
           "\tcancel event",
           "\tsend \"You cannot teleport outside the world border!\""})
@Since("1.0.0")
public class CondIsInWorldBorder extends PropertyCondition<Location> {

    static {
        register(CondIsInWorldBorder.class, "(within|inside) [the] world[(-| )]border", "locations");
    }

    @Override
    public boolean check(Location location) {
        return location.getWorld().getWorldBorder().isInside(location);
    }

    @Override
    protected String getPropertyName() {
        return "inside the world border";
    }

}
