package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.utils.Utils;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Rainbow Text")
@Description({"Returns the specified text in rainbow.",
              "Just a note this looks crazy in console, but theres nothing we can do about it:p (like this: \"\")"})
@Examples("broadcast pastel rainbow \"hi! this is a pastel rainbow string\"")
@RequiredPlugins("Spigot 1.16+")
@Since("1.0.0")
public class ExprRainbow extends SimpleExpression<String> {

    static {
        if (Utils.getServerMajorVersion() <= 16) {
            Skript.registerExpression(ExprRainbow.class, String.class, ExpressionType.SIMPLE,
                    "(1¦pastel rainbow|2¦monochrome |[normal ]rainbow ) %string%");
        }
    }

    private boolean pastel, monochrome;
    private Expression<String> message;

    @Nullable
    @Override
    protected String[] get(Event e) {
        String str = message.getSingle(e);
        if (str == null) return new String[0];
        return new String[]{monochrome ? Utils.monochrome(str) : Utils.simpleRainbow(str, pastel)};
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
        return monochrome ? "monochrome" + message.toString(e, debug) :
                (pastel ? "" : "pastel ") + "rainbow " + message.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        pastel = parseResult.mark == 1;
        monochrome = parseResult.mark == 2;
        message = (Expression<String>) exprs[0];
        return true;
    }
}
