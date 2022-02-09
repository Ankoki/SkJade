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
import com.ankoki.pastebinapi.enums.PasteVisibility;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Set Paste Visibility")
@Description("Sets the visibility of a paste.")
@Examples("make the paste with the id \"myPaste\" be public")
@Since("1.0.0")
public class EffSetPasteVisibility extends Effect {

    static {
        Skript.registerEffect(EffSetPasteVisibility.class,
                "make %pastes% be (public|1¦private|2¦unlisted)");
    }

    private Expression<PasteBuilder> pasteBuilder;
    private PasteVisibility visiblity;

    @Override
    protected void execute(Event e) {
        PasteBuilder[] builders = pasteBuilder.getArray(e);
        if (builders.length < 1) return;
        Arrays.stream(builders).forEach(builder -> PasteManager.setVisibility(builder, visiblity));
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make " + pasteBuilder.toString(e, debug) + " be public, private or unlisted";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        switch (parseResult.mark) {
            case 1:
                visiblity = PasteVisibility.UNLISTED;
                break;
            case 2:
                visiblity = PasteVisibility.PRIVATE;
                break;
            default:
                visiblity = PasteVisibility.PUBLIC;
        }
        pasteBuilder = (Expression<PasteBuilder>) exprs[0];
        return true;
    }
}
