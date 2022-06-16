package com.ankoki.skjade.elements.binflop.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Binflop Link")
@Description({"Only usable in a Upload Binflop section.",
        "Contains the link of the last uploaded binflop."})
@Examples("""
        upload new binflop with text "1. PROFIT", "2. LOSS" and "3. DATA":
            send "Binflop created:" and " - %binflop-link%" to console
        """)
@Since("2.0")
public class ExprBinflopLink extends SimpleExpression<String> {
    protected static String LAST_LINK = "NOT EXECUTED";

    static {
        Skript.registerExpression(ExprBinflopLink.class, String.class, ExpressionType.SIMPLE, "binflop(-| )link");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult) {
        SkriptEvent event = getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecBinflopCreate.class)) return true;
        Skript.error("You can only use a binflop-link after uploading. Cache if need-be.");
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "binflop link";
    }

    @Override
    protected @Nullable String[] get(Event event) {
        return new String[]{LAST_LINK};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
