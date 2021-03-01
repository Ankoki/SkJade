package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.Nullable;

@Name("Inet Address")
@Description("Used for event-address in the AsyncPlayerPreLoginEvent")
@Examples("send \"event-address\"")
@Since("1.1.0")
public class ExprInetAddress extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprInetAddress.class, String.class, ExpressionType.SIMPLE,
                "event(-| )[inet]address");
    }
    @Nullable
    @Override
    protected String[] get(Event e) {
        if (!(e instanceof AsyncPlayerPreLoginEvent)) return null;
        return new String[]{((AsyncPlayerPreLoginEvent) e).getAddress().toString()};
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
    public String toString(@Nullable Event e, boolean debug) {
        return "event-address";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (ScriptLoader.isCurrentEvent(AsyncPlayerPreLoginEvent.class)) {
            return true;
        }
        Skript.error("You cannot use event-address outside of an async prelogin event!");
        return false;
    }
}
