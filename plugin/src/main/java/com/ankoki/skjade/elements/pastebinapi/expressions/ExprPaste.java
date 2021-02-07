package com.ankoki.skjade.elements.pastebinapi.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPaste extends SimpleExpression<PasteBuilder> {

    static {
        Skript.registerExpression(ExprPaste.class, PasteBuilder.class, ExpressionType.SIMPLE,
                "[the] paste[builder] with [the] id %string%");
    }

    private Expression<String> pasteId;

    @Nullable
    @Override
    protected PasteBuilder[] get(Event e) {
        String id = pasteId.getSingle(e);
        if (id == null || id.isEmpty()) return null;
        return new PasteBuilder[]{PasteManager.getFromID(id)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PasteBuilder> getReturnType() {
        return PasteBuilder.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the paste with id " + pasteId.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteId = (Expression<String>) exprs[0];
        return true;
    }
}
