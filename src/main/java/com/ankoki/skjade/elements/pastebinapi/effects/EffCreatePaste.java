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
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Create Paste")
@Description("Creates a new pastebin paste. The ID must be unique.")
@Examples("create a new paste with the id \"myPaste\"")
@Since("1.0.0")
public class EffCreatePaste extends Effect {

    static {
        Skript.registerEffect(EffCreatePaste.class,
                "create [a] [new] paste[bin[ paste]] with [the] id %string%");
    }

    private Expression<String> pasteID;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pasteID = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = pasteID.getSingle(e);
        if (id == null || id.isEmpty()) return;
        PasteManager.createPasteBuilder(id);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create a new paste with the id " + pasteID.toString(e, debug);
    }
}
