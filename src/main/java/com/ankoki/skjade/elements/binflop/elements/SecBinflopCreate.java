package com.ankoki.skjade.elements.binflop.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.ankoki.roku.web.JSON;
import com.ankoki.roku.web.WebRequest;
import com.ankoki.roku.web.exceptions.MalformedJsonException;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Name("Upload Binflop")
@Description({"Creates a new binflop post with the given text.",
                "If multiple texts are given, they will be split by \"\\n\".",
                "In this section you can use the binflop-link expression to get the link which contains your text."})
@Examples("""
        upload new binflop with text "1. PROFIT", "2. LOSS" and "3. DATA":
            send "Binflop created:" and " - %binflop-link%" to console
        """)
@Since("2.0")
public class SecBinflopCreate extends Section {

    static {
        Skript.registerSection(SecBinflopCreate.class,
                "(upload|create) new binflop (x:with hidden ips and|with) text %strings%");
    }

    private Trigger trigger;
    private Expression<String> textExpr;
    private boolean hide;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        if (getParser().isCurrentSection(SecBinflopCreate.class) || getParser().isCurrentSection(SecBinflopRead.class)) {
            Skript.error("You can't upload another paste in an upload or read paste section.");
            return false;
        }
        TriggerItem item = first;
        while (item != null) {
            if (item instanceof Delay) {
                Skript.error("You cannot wait after uploading a paste.");
                return false;
            } item = item.getNext();
        }
        textExpr = (Expression<String>) exprs[0];
        hide = parseResult.hasTag("x");
        trigger = loadCode(sectionNode, "binflop execute", getParser().getCurrentEvents());
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        String[] text = textExpr.getArray(event);
        TriggerItem item = walk(event, false);
        try {
            WebRequest request = new WebRequest("https://bin.birdflop.com/documents", WebRequest.RequestType.POST);
            request.addParameter("data", String.join("\n", text));
            request.addParameter("hide_ips", Boolean.toString(hide));
            CompletableFuture.supplyAsync(() -> {
                try {
                    Optional<String> optional = request.execute();
                    if (optional.isEmpty()) return 0;
                    JSON json = new JSON(optional.get());
                    ExprBinflopLink.LAST_LINK = "https://bin.birdflop.com/" + json.get("key");
                    trigger.execute(event);
                    return 1;
                } catch (IOException | MalformedJsonException ex) {
                    ex.printStackTrace();
                } return -1;
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "upload new binflop " + (hide ? "with hidden ips and" : "with") + " text " + textExpr.toString(event, b);
    }
}
