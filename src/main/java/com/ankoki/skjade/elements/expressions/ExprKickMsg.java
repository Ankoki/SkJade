package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.eclipse.jdt.annotation.Nullable;

@Name("Kick Message")
@Description("Gets/sets the kick message in an AsyncPlayerPreLoginEvent")
@Examples("set kick-message to \"lol u got kicked\"")
@Since("1.1.0")
public class ExprKickMsg extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprKickMsg.class, String.class, ExpressionType.SIMPLE,
                "[event(-| )]kick[( |-)]message");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (ParserInstance.get().isCurrentEvent(AsyncPlayerPreLoginEvent.class)) {
            return true;
        }
        Skript.error("You cannot use event-address outside of an async prelogin event!");
        return false;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (!(e instanceof AsyncPlayerPreLoginEvent)) {
            return new String[0];
        }
        return new String[]{((AsyncPlayerPreLoginEvent) e).getKickMessage()};
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
        return "event-kick-message";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (delta.length > 1 && delta[0] instanceof String && e instanceof AsyncPlayerPreLoginEvent) {
            ((AsyncPlayerPreLoginEvent) e).setKickMessage((String) delta[0]);
        }
    }
}
