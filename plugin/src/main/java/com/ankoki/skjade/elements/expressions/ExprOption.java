package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprOption extends SimpleExpression<String> {

    static {
        /*Skript.registerExpression(ExprOption.class, String.class, ExpressionType.SIMPLE,
                "[the] option %string% from [[the] s(k|c)ript [file]] %string%");*/
    }

    private Expression<String> optionName;
    private Expression<String> scriptName;

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (optionName == null || scriptName == null) return null;
        String option = optionName.getSingle(e);
        String script = scriptName.getSingle(e);
        if (option == null || script == null || option.isEmpty() || script.isEmpty()) return null;
        //where the fuck am i meant to get this from mehskam
        return new String[0];
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
    public String toString(@Nullable Event e, boolean debug) {
        return "option " + optionName.toString(e, debug) + " from the script " + scriptName.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        optionName = (Expression<String>) exprs[0];
        scriptName = (Expression<String>) exprs[1];
        return true;
    }
}
