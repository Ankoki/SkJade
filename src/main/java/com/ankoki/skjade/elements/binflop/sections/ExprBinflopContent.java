package com.ankoki.skjade.elements.binflop.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class ExprBinflopContent  extends SimpleExpression<String> {
    protected static String LAST_DATA = "NOT EXECUTED";

    static {
        Skript.registerExpression(ExprBinflopContent.class, String.class, ExpressionType.SIMPLE, "binflop(-| )(response|data|content)");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        SkriptEvent event = getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecBinflopRead.class)) return true;
        Skript.error("You can only use the binflop-content after reading. Cache if need-be.");
        return false;
    }

    @Override
    protected @Nullable String[] get(Event event) {
        return new String[]{LAST_DATA};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "binflop content";
    }
}
