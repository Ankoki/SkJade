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
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Set Paste Format")
@Description("Sets the formattinng of a paste. You can find more about the formatting options here: https://pastebin.com/doc_api#5")
@Examples("set the format of the paste with the id \"myPaste\" to \"java\"")
@Since("1.0.0")
public class EffSetPasteFormat extends Effect {

    static {
        Skript.registerEffect(EffSetPasteFormat.class,
                "set [the] [text] format[ting] of %pastes% to %string%");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private Expression<String> formatting;

    @Override
    protected void execute(Event e) {
        PasteBuilder[] builders = pasteBuilder.getArray(e);
        String format = formatting.getSingle(e);
        if (builders.length > 0 && format != null && !format.isEmpty()) {
            Arrays.stream(builders).forEach(builder -> PasteManager.setFormat(builder, format));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set the format of " + pasteBuilder.toString(e, debug) + " to " + formatting.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteBuilder = (Expression<PasteBuilder>) exprs[0];
        formatting = (Expression<String>) exprs[1];
        return true;
    }
}
