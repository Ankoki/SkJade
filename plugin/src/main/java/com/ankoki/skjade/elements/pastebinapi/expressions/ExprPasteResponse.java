package com.ankoki.skjade.elements.pastebinapi.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.response.Response;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@Name("Paste Response")
@Description("Returns the raw text from a pastebin. The developer key is linked to your pastebin account and found at this website: https://pastebin.com/doc_api#1")
@Examples("set {_pastebin} to the paste repsone from \"bpgexBcS\" with the developer key {@apiKey}")
@Since("1.0.0")
public class ExprPasteResponse extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPasteResponse.class, String.class, ExpressionType.SIMPLE,
                "[the] [paste] response from [[the] paste] %string% with the dev[eloper] key %string%");
    }

    private Expression<String> pasteKey, developerKey;

    @Nullable
    @Override
    protected String[] get(Event e) {
        String key = pasteKey.getSingle(e);
        String devKey = developerKey.getSingle(e);
        if (key == null || devKey == null ||
        key.isEmpty() || devKey.isEmpty()) return null;

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Pastebin pastebin = SkJade.getFactory().createPastebin(devKey);
            Response<String> pasteResponse = pastebin.getRawPaste(key);
            if (pasteResponse.hasError()) {
                Skript.error(pasteResponse.getError());
                return null;
            }
            return pasteResponse.get();
        });
        Delay.addDelayedEvent(e);
        try {
            return new String[]{future.get()};
        } catch (Exception ex) { return null; }
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
        return "paste response from " + pasteKey.toString(e, debug) + " with the developer key " + developerKey.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteKey = (Expression<String>) exprs[0];
        developerKey = (Expression<String>) exprs[1];
        return true;
    }
}
