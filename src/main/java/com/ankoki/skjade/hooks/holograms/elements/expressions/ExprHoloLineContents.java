package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Line Content")
@Description("Gets the content, text, material, item or entity of a hologram line.")
@Examples("broadcast \"%material of line 5 of {_holo}%\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprHoloLineContents extends SimpleExpression<Object> {

	private static final String[] CONTENT_TYPES = new String[]{"content", "text", "material", "item", "entity"};
	private static final Class<?>[] CLASS_TYPES = new Class<?>[]{String.class, String.class, Material.class, ItemStack.class, EntityType.class};

	static {
		Skript.registerExpression(ExprHoloLineContents.class, Object.class, ExpressionType.SIMPLE,
				"[the] (:content|:text|:material|:item|:entity) of %skjhololine%",
				"%skjhololine%'s (:content|:text|:material|:item|:entity)");
	}

	private Expression<SKJHoloLine> lineExpr;
	private String tag;
	private Class<?> returnType;

	@Override
	public boolean init(Expression<?>[] exprs, int pattern, Kleenean kleenean, ParseResult result) {
		for (int i = 0; i < CONTENT_TYPES.length; i++) {
			if (result.hasTag(CONTENT_TYPES[i])) {
				tag = CONTENT_TYPES[i];
				returnType = CLASS_TYPES[i];
			}
		}
		if (returnType == EntityType.class && !HoloHandler.get().getCurrentProvider().supportsEntityLines()) {
			Skript.error("You have tried to get an entity line, when your hologram provider does not support such.");
			return false;
		}
		lineExpr = (Expression<SKJHoloLine>) exprs[0];
		return true;
	}

	@Override
	protected @Nullable Object[] get(Event event) {
		SKJHoloLine line = lineExpr.getSingle(event);
		if (line == null)
			return new Object[0];
		Object object = null;
		switch (tag.toUpperCase()) {
			case "CONTENT" -> object = line.getContent();
			case "TEXT" -> object = line.getText();
			case "MATERIAL" -> object = line.getMaterial();
			case "ITEM" -> object = line.getItem();
			case "ENTITY" -> object = line.getEntity();
		} return new Object[]{object};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<?> getReturnType() {
		return returnType;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return lineExpr.toString(event, b) + "'s " + tag;
	}
}
