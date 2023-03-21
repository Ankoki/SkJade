package com.ankoki.skjade.hooks.holograms.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.elements.structures.StructPlaceholder;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Name("Placeholder Return")
@Description("Used in placeholder creation structures to return the wanted value.")
@Examples({"register placeholder:",
		"\tname: random_number",
		"\trefresh rate: 1 second",
		"\ttrigger:",
		"\t\treturn \"%random int between 0 and 100%\" as placeholder value"})
@Since("2.0")
@RequiredPlugins("Holographic Displays")
public class EffPlaceholderReturn extends Effect {

	private static final Map<Structure, String> VALUES = new ConcurrentHashMap<>();

	/**
	 * Returns the placeholder value of a sections return statement. Must have the trigger executed beforehand.
	 * @param structure the section to get.
	 * @return the return value.
	 */
	public static String getValue(Structure structure) {
		return VALUES.getOrDefault(structure, "<none>");
	}

	static {
		if (HoloHandler.get().getCurrentProvider().supportsCustomPlaceholders())
			Skript.registerEffect(EffPlaceholderReturn.class,
					"return %string% as [the] placeholder [value]");
	}

	private Expression<String> returnExpr;
	private Structure structure;

	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		returnExpr = (Expression<String>) exprs[0];
		structure = this.getParser().getCurrentStructure();
		return true;
	}

	@Override
	protected void execute(Event event) {
		String rtrn = returnExpr.getSingle(event);
		if (rtrn != null)
			VALUES.put(structure, rtrn);
	}

	@Override
	public List<Class<? extends Structure>> getUsableStructures() {
		return List.of(StructPlaceholder.class);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "return " + returnExpr.toString(event, debug) + " as the placeholder value";
	}

}
