package com.ankoki.skjade.elements.binflop.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.ankoki.roku.web.JSONWrapper;
import com.ankoki.roku.web.WebRequest;
import com.ankoki.roku.web.exceptions.MalformedJsonException;
import com.ankoki.skjade.SkJade;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SecBinflop extends Section {

    static {
        Skript.registerSection(SecBinflop.class,
                "upload new binflop (x:with hidden ips and|with) text %strings%");
    }

    private Trigger trigger;
    private Expression<String> textExpr;
    private boolean hide;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
        if (getParser().isCurrentSection(SecBinflop.class)) {
            Skript.error("You can't upload another paste in an upload paste section.");
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
                    JSONWrapper json = new JSONWrapper(optional.get());
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
