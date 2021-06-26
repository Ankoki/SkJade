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
import org.jetbrains.annotations.Nullable;

@Name("Character")
@Description("A single character.")
@Examples("broadcast 'h'")
@Since("1.1.0")
public class ExprCharacter extends SimpleExpression<Character> {

    static {
        Skript.registerExpression(ExprCharacter.class, Character.class, ExpressionType.SIMPLE,
                "'<(.)>'");
    }

    private String string;

    @Nullable
    @Override
    protected Character[] get(Event e) {
        if (string == null || string.isEmpty()) return new Character[0];
        return new Character[]{string.toCharArray()[0]};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Character> getReturnType() {
        return Character.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "'" + string + "'";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        string = parseResult.regexes.get(0).group(1);
        return true;
    }
}
