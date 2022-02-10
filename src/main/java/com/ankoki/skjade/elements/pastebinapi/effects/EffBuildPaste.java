package com.ankoki.skjade.elements.pastebinapi.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.pastebinapi.api.PasteBuilder;
import com.ankoki.pastebinapi.utils.Response;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Build Paste")
@Description("Create's a pastebin paste. You should only call this once per paste. The developer key is linked to your pastebin account and found at this website: https://pastebin.com/doc_api#1")
@Examples({"build the paste with the id \"myPaste\" with the developer key \"{@developerKey}\"",
           "send the link of last built pastebin"})
@Since("1.0.0")
public class EffBuildPaste extends Effect {

    static {
        Skript.registerEffect(EffBuildPaste.class,
                "build [the] [paste] %paste% with the dev[eloper] key %string%");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private Expression<String> developerKey;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteBuilder = (Expression<PasteBuilder>) exprs[0];
        developerKey = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        PasteBuilder builder = pasteBuilder.getSingle(e);
        String devKey = developerKey.getSingle(e);
        if (builder == null || devKey == null || devKey.isEmpty()) return;
        builder.setDeveloperKey(devKey);
        Bukkit.getScheduler().runTaskAsynchronously(SkJade.getInstance(), () -> {
            Response<String> response = builder.createPaste();
            if (response.hasError()) {
                PasteManager.setLinkToLastBuiltPaste("There was an error building your paste! \n" +
                        response.getError());
            } else {
                PasteManager.setLinkToLastBuiltPaste(response.get());
            }
        });
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "build " + pasteBuilder.toString(e, debug) + " with the developer key " + developerKey.toString(e, debug);
    }
}