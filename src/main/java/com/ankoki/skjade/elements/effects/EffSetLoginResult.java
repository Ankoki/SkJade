package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.eclipse.jdt.annotation.Nullable;

public class EffSetLoginResult extends Effect {

	static {
		Skript.registerEffect(EffSetLoginResult.class, "set [event( |-)]login[( |-)]result to %string% with message %string%");
	}

	private Expression<String> resultExpr, messageExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		if (!ParserInstance.get().isCurrentEvent(AsyncPlayerPreLoginEvent.class)) {
			Skript.error("You cannot use login-result outside an async prelogin event!");
			return false;
		}
		resultExpr = (Expression<String>) exprs[0];
		messageExpr = (Expression<String>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		String result = resultExpr.getSingle(event);
		String message = messageExpr.getSingle(event);
		if (result == null || message == null)
			return;
		switch ((result).toLowerCase()) {
			case "kick other":
				((AsyncPlayerPreLoginEvent) event).disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
				break;
			case "allowed":
				((AsyncPlayerPreLoginEvent) event).disallow(AsyncPlayerPreLoginEvent.Result.ALLOWED, message);
				break;
			case "kick banned":
				((AsyncPlayerPreLoginEvent) event).disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
				break;
			case "kick full":
				((AsyncPlayerPreLoginEvent) event).disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, message);
				break;
			case "kick whitelist":
				((AsyncPlayerPreLoginEvent) event).disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, message);
				break;
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "set login-result to " + resultExpr.toString(event, b) + " with message " + messageExpr.toString(event, b);
	}
}
