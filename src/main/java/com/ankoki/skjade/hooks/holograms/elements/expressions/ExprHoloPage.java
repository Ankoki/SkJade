package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skjade.hooks.holograms.api.HoloManager;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Hologram Page")
@Description("Gets/sets the pages of a hologram.")
@Examples("set page 1 of holo keyed as \"aespa\" to \"hey!\", glowing iron sword, \"This is a sword lol\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class ExprHoloPage extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprHoloPage.class, Object.class, ExpressionType.SIMPLE,
                "(page %-number%|[default ]lines) of %skjholo%");
    }

    private Expression<Number> indexExpr;
    private Expression<SKJHolo> holoExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        indexExpr = (Expression<Number>) exprs[0];
        holoExpr = (Expression<SKJHolo>) exprs[1];
        return true;
    }

    @Override
    protected @Nullable Object[] get(Event event) {
        SKJHolo holo = holoExpr.getSingle(event);
        if (holo == null) return new Object[0];
        int index = 0;
        if (indexExpr != null) {
            Number number = indexExpr.getSingle(event);
            if (number == null) return new Object[0];
            index = HoloManager.get().getCurrentProvider().supportsPages() ? number.intValue() : 0;
        }
        List<SKJHoloLine> page = holo.getPage(index);
        return SKJHoloLine.transform(page);
    }

    @Override
    public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
        return CollectionUtils.array(Object.class);
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        if (delta.length == 0 && (mode != ChangeMode.RESET && mode != ChangeMode.REMOVE_ALL)) return;
        SKJHolo holo = holoExpr.getSingle(event);
        if (holo == null) return;
        int index = 0;
        if (indexExpr != null) {
            Number number = indexExpr.getSingle(event);
            if (number == null) return;
            index = HoloManager.get().getCurrentProvider().supportsPages() ? number.intValue() : 0;
        }

        switch (mode) {
            case ADD -> holo.appendLine(index, HoloManager.get().getCurrentProvider().parseLine(delta[0]));
            case SET -> holo.setLines(index, HoloManager.get().getCurrentProvider().parseLines(Arrays.asList(delta)));
            case REMOVE -> {
                List<SKJHoloLine> current = holo.getPage(index);
                List<SKJHoloLine> updated = new ArrayList<>();
                Object object = delta[0];
                for (SKJHoloLine line : current) {
                    if (line.get() != object) updated.add(line);
                }
                holo.setLines(index, updated);
            }
            default -> holo.setLines(index, List.of());
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return null;
    }
}
