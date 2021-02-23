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
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.Nullable;

@Name("Login Result")
@Description("Gets/sets the login result for an async prelogin event. Please note this can only be set to \"kick other\", \"allowed\", \"kick banned\", \"kick full\", or \"kick whitelist\"")
@Examples("broadcast event-login-result")
@Since("insert version")
public class ExprLoginResult extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprLoginResult.class, String.class, ExpressionType.SIMPLE,
                "event( |-)login[( |-)]result");
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        if (!(e instanceof AsyncPlayerPreLoginEvent)) return null;
        return new String[]{((AsyncPlayerPreLoginEvent) e).getLoginResult().name().replace("_", " ").toLowerCase()};
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
        return "event-login-result";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(AsyncPlayerPreLoginEvent.class)) {
            Skript.error("You cannot use event-login-result outside an async prelogin event!");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        if (mode != ChangeMode.SET) return null;
        return CollectionUtils.array(String.class);
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        if (!(e instanceof AsyncPlayerPreLoginEvent)) return;
        if (mode != ChangeMode.SET) return;
        switch (((String) delta[0]).toLowerCase()) {
            case "kick other":
                ((AsyncPlayerPreLoginEvent) e).setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                break;
            case "allowed":
                ((AsyncPlayerPreLoginEvent) e).setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
                break;
            case "kick banned":
                ((AsyncPlayerPreLoginEvent) e).setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                break;
            case "kick full":
                ((AsyncPlayerPreLoginEvent) e).setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_FULL);
                break;
            case "kick whitelist":
                ((AsyncPlayerPreLoginEvent) e).setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                break;
        }
    }
}
