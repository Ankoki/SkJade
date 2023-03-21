package com.ankoki.skjade.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionList;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.registrations.EventValues;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

@Name("Send Mini Message")
@Description("Sends a mini message text.")
@Since("2.0")
@Examples("send mini-messaged \"<rainbow>hello!</rainbow>\" to player")
public class EffSendMiniMessage extends Effect {

	static {
		if (Skript.classExists("net.kyori.adventure.text.minimessage.MiniMessage"))
			Skript.registerEffect(EffSendMiniMessage.class, "(message|send) mini(-| )message[d] %strings% [to %-commandsenders%]");

	}

	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
	private Expression<String> messageExpr;
	private Expression<?>[] messages;
	private Expression<CommandSender> senderExpr;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		messageExpr = (Expression<String>) exprs[0];
		messages = messageExpr instanceof ExpressionList ?
				((ExpressionList<?>) messageExpr).getExpressions() : new Expression[]{messageExpr};
		senderExpr = (Expression<CommandSender>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		CommandSender[] senders;
		CommandSender value = EventValues.getEventValue(event, CommandSender.class, 0);
		if (senderExpr == null && value != null) senders = new CommandSender[]{value};
		else if (senderExpr != null) senders = senderExpr.getAll(event);
		else {
			Skript.error("You have not declared a player to send the message to outside of a player event.");
			return;
		}
		for (Expression<?> expr : this.getMessages()) {
			String text;
			if (expr instanceof VariableString string) text = string.toUnformattedString(event);
			else text = String.valueOf(expr.getSingle(event));
			if (text == null) continue;
			for (CommandSender sender : senders) sender.sendMessage(MINI_MESSAGE.deserialize(text));
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "send mini-messaged " + messageExpr.toString(e, debug) + (senderExpr != null ? " to " + senderExpr.toString(e, debug) : "");
	}

	private Expression<?>[] getMessages() {
		if (messageExpr instanceof ExpressionList && !messageExpr.getAnd()) return new Expression[]{CollectionUtils.getRandom(messages)};
		return messages;
	}

}