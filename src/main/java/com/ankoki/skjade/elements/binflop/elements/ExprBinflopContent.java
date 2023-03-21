package com.ankoki.skjade.elements.binflop.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Binflop Content")
@Description({"Only usable in a Read Binflop section.",
                "Contains the text from the last read binflop."})
@Examples("""
        read binflop with key "r9j2mf":
            send "Binflop content:" and " - %binflop-content%" to console
        """)
@Since("2.0")
public class ExprBinflopContent extends SimpleExpression<String> {
    protected static String LAST_DATA = "NOT EXECUTED";

    static {
        Skript.registerExpression(ExprBinflopContent.class, String.class, ExpressionType.SIMPLE, "binflop(-| )(response|data|content)");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult) {
        SkriptEvent event = getParser().getCurrentSkriptEvent();
        if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecBinflopRead.class)) return true;
        Skript.error("You can only use the binflop-content after reading. Cache if need-be.");
        return false;
    }

    @Override
    protected @Nullable String[] get(Event event) {
        return new String[]{LAST_DATA};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "binflop content";
    }
}
