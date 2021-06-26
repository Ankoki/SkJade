package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Contents of Hologram Line")
@Description("Returns the text of a hologram line, or an itemstack.")
@Examples("broadcast content of event-line")
@RequiredPlugins("HolographicDisplays")
@Since("INSERT VERSION")
public class ExprHologramLineContents extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprHologramLineContents.class, Object.class, ExpressionType.SIMPLE,
                "[skjade] [the] (content[s]|text|item) (of|in|at) [[the] holo[gram]] %hologramline%");
    }

    private Expression<HologramLine> lineExpr;

    @Nullable
    @Override
    protected Object[] get(Event e) {
        HologramLine line = lineExpr.getSingle(e);
        if (line instanceof TextLine) {
            return new Object[]{((TextLine) line).getText()};
        } else if (line instanceof ItemLine) {
            return new Object[]{((ItemLine) line).getItemStack()};
        }
        return new Object[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "contents of " + lineExpr.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        lineExpr = (Expression<HologramLine>) exprs[0];
        return true;
    }
}
