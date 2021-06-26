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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Set Paste Text")
@Description("Sets the text of a paste.")
@Examples("set the raw text of the paste with id \"myPaste\" to \"Hi! This is my paste!\"")
@Since("1.0.0")
public class EffSetPasteText extends Effect {

    static {
        Skript.registerEffect(EffSetPasteText.class,
                "set [the] [raw] text of %pastes% to %strings%");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private Expression<String> text;

    @Override
    protected void execute(Event e) {
        PasteBuilder[] builders = pasteBuilder.getArray(e);
        String[] strs = text.getArray(e);
        if (strs.length < 1 || builders.length < 1) return;
        StringBuilder b = new StringBuilder();
        for (String s : strs) {
            b.append(s).append("\n");
        }
        Arrays.stream(builders).forEach(builder -> PasteManager.setText(builder, b.toString()));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set the raw text of " + pasteBuilder.toString(e, debug) + " to " + text.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteBuilder = (Expression<PasteBuilder>) exprs[0];
        text = (Expression<String>) exprs[1];
        return true;
    }
}
