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
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Flipped Boolean")
@Description("Returns the flipped value of a boolean")
@Examples({"broadcast \"%flipped {bed::%player's uuid%}%\"",
            "set player's flight mode to !(player's flight mode)"})
@Since("1.0.0")
public class ExprFlippedBool extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprFlippedBool.class, Boolean.class, ExpressionType.COMBINED,
            "(flipped |toggled |inverted |!)%boolean%");
    }

    private Expression<Boolean> bool;

    @Nullable
    @Override
    protected Boolean[] get(Event e) {
        if (bool == null) return new Boolean[]{false};
        return new Boolean[]{Boolean.FALSE.equals(bool.getSingle(e)) ? Boolean.TRUE : Boolean.FALSE};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "flipped " + bool.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        bool = (Expression<Boolean>) exprs[0];
        return true;
    }
}
