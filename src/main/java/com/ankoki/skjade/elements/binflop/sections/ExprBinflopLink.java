package com.ankoki.skjade.elements.binflop.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprBinflopLink extends SimpleExpression<String> {
    protected static String LAST_LINK = "<none>";

    static {
        Skript.registerExpression(ExprBinflopLink.class, String.class, ExpressionType.SIMPLE, "binflop(-| )link");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult) {
        if (getParser().isCurrentSection(SecBinflop.class)) return true;
        Skript.error("You can only use a binflop-link after uploading. Cache if needbe.");
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "binflop link";
    }

    @Override
    protected @Nullable String[] get(Event event) {
        return new String[]{LAST_LINK};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
