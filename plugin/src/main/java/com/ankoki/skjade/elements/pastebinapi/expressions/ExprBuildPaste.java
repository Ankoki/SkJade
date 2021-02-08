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
import com.ankoki.pastebinapi.utils.Response;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Build Paste")
@Description("Create's a pastebin paste. Returns the link of the built pastebin. Only call this once per builder and store the link. The developer key is linked to your pastebin account and found at this website: https://pastebin.com/doc_api#1")
@Examples("send \"the url of the built paste with the id \"myPaste\" with the developer key \"{@developerKey}\"")
@Since("1.0.0")
public class ExprBuildPaste extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprBuildPaste.class, String.class, ExpressionType.SIMPLE,
                "[the] (link|url) of [the] built %paste% with [the] dev[elepor] key %string%");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private Expression<String> developerKey;

    @Nullable
    @Override
    protected String[] get(Event e) {
        PasteBuilder builder = pasteBuilder.getSingle(e);
        String devKey = developerKey.getSingle(e);
        if (builder == null || devKey == null || devKey.isEmpty()) return null;
        builder.setDeveloperKey(devKey);
        Response<String> postResult = builder.createPaste();
        if (postResult.hasError()) {
            return new String[]{"There was an error posting your pastebin! \n" + postResult.getError()};
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
