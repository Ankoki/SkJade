package com.ankoki.skjade.elements.pastebinapi.expressions;

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
import com.ankoki.pastebinapi.api.PasteBuilder;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Paste")
@Description("Returns the pastebin paste with that id if it exists, else, it will be null.")
@Examples("set {logs::%player's uuid%} to the pastebuilder with the id \"%player%Paste\"")
@Since("1.0.0")
public class ExprPaste extends SimpleExpression<PasteBuilder> {

    static {
        Skript.registerExpression(ExprPaste.class, PasteBuilder.class, ExpressionType.SIMPLE,
                "[the] paste[builder] with [the] id %string%");
    }

    private Expression<String> pasteId;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteId = (Expression<String>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected PasteBuilder[] get(Event e) {
        String id = pasteId.getSingle(e);
        if (id == null || id.isEmpty()) return new PasteBuilder[0];
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
}
