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
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Name("Read Binflop")
@Description({"Retrieves the text from a binflop with the given key.",
        "In this section you can use the binflop-content expression to get the link which contains the text."})
@Examples("""
        read binflop with key "r9j2mf":
            send "Binflop content:" and " - %binflop-content%" to console
        """)
@Since("2.0")
public class SecBinflopRead extends Section {

    static {
        Skript.registerSection(SecBinflopRead.class, "read binflop with key %string%");
    }

    private Expression<String> keyExpr;
    private Trigger trigger;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        if (getParser().isCurrentSection(SecBinflopCreate.class) || getParser().isCurrentSection(SecBinflopRead.class)) {
            Skript.error("You can't upload another paste in an upload or read paste section.");
            return false;
        }
        TriggerItem item = first;
        while (item != null) {
            if (item instanceof Delay) {
                Skript.error("You cannot wait after reading a paste.");
                return false;
            } item = item.getNext();
        }
        keyExpr = (Expression<String>) exprs[0];
        trigger = loadCode(sectionNode, "binflop upload", getParser().getCurrentEvents());
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        TriggerItem item = walk(event, false);
        String key = keyExpr.getSingle(event);
        if (key == null) key = "NO KEY GIVEN";
        if (!key.startsWith("https://bin.birdflop.com/documents/")) key = "https://bin.birdflop.com/documents/" + key;
        String finalKey = key;
        CompletableFuture.supplyAsync(() -> {
            try {
                WebRequest request = new WebRequest(finalKey, WebRequest.RequestType.GET);
                Optional<String> optional = request.execute();
                if (optional.isPresent()) {
                    JSON json = new JSON(optional.get());
                    ExprBinflopContent.LAST_DATA = json.containsKey("data") ? (String) json.get("data") : "No paste with id ID '" + finalKey + "'.";
                } else ExprBinflopContent.LAST_DATA = "No response.";
            } catch (IOException | MalformedJsonException ex) {
                if (ex instanceof MalformedURLException) ExprBinflopContent.LAST_DATA = "No Key Given.";
                else ex.printStackTrace();
            } finally {
                trigger.execute(event);
            }
            return -1;
        });
        return item;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "read binflop with key " + keyExpr.toString(event, debug);
    }
}
