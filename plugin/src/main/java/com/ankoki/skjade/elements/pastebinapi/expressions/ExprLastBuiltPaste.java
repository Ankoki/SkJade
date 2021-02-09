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
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Last Built Paste")
@Description("Returns the link of the last built pastebin. If you build a new paste, this will be overridden.")
@Examples("send the url of the last built paste")
@Since("1.0.0")
public class ExprLastBuiltPaste extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprLastBuiltPaste.class, String.class, ExpressionType.SIMPLE,
                "[the] [(link|url) of] [the] last built paste[bin [paste]]");
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        return new String[]{PasteManager.getLinkToLastBuiltPaste()};
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
        return "the url of the last built paste";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }
}
