package com.ankoki.skjade.elements.pastebinapi.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.response.Response;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprBuildPaste extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprBuildPaste.class, String.class, ExpressionType.SIMPLE,
                "[the] (link|url) of built %paste% with [the] dev[elepor] key %string%");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private Expression<String> developerKey;

    @Nullable
    @Override
    protected String[] get(Event e) {
        PasteBuilder builder = pasteBuilder.getSingle(e);
        String devKey = developerKey.getSingle(e);
        if (builder == null || devKey == null || devKey.isEmpty()) return null;

        Pastebin pastebin = SkJade.getFactory().createPastebin(devKey);
        Paste paste = builder.build();
        if (paste == null || pastebin == null) return null;
        //probably gotta send my own thing from the pastebuilder coz it seems the api is too old to work for this.
        Response<String> postResult = pastebin.post(paste);
        if (postResult.hasError()) {
            Skript.error(postResult.getError());
        }

        return new String[]{postResult.get()};
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
        return "the url of built " + pasteBuilder.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteBuilder = (Expression<PasteBuilder>) exprs[0];
        developerKey = (Expression<String>) exprs[1];
        return true;
    }
}
