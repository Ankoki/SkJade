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
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Set Paste Title")
@Description("Sets the title of a paste.")
@Examples("set the title of the paste with id \"myPaste\" to \"Hi! This is my paste!\"")
@Since("1.0.0")
public class EffSetPasteTitle extends Effect {

    static {
        Skript.registerEffect(EffSetPasteTitle.class,
                "set [the] title of %pastes% to %string%");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private Expression<String> title;

    @Override
    protected void execute(Event e) {
        PasteBuilder[] builders = pasteBuilder.getArray(e);
        String t = title.getSingle(e);
        if (t == null || t.isEmpty() || builders.length < 1) return;
        Arrays.stream(builders).forEach(builder -> PasteManager.setTitle(builder, t));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set the title of " + pasteBuilder.toString(e, debug) + " to " + title.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteBuilder = (Expression<PasteBuilder>) exprs[0];
        title = (Expression<String>) exprs[1];
        return true;
    }
}
