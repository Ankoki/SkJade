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
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram")
@Description("Gets/deletes a hologram via its key.")
@Examples("set {_holo} to holo keyed as \"so bad by stayc\"")
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class ExprHologram extends SimpleExpression<SKJHolo> {

    static {
        Skript.registerExpression(ExprHologram.class, SKJHolo.class, ExpressionType.COMBINED,
                "[the] [skjade] holo[gram] [(key|nam)ed [as]] %string%");
    }

    private Expression<String> keyExpr;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        keyExpr = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected @Nullable SKJHolo[] get(Event event) {
        String key = keyExpr.getSingle(event);
        if (key == null)
            return new SKJHolo[0];
        return new SKJHolo[]{HoloHandler.get().getHolo(key)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SKJHolo> getReturnType() {
        return SKJHolo.class;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (mode != ChangeMode.DELETE)
            return;
        String key = keyExpr.getSingle(e);
        if (key == null)
            return;
        SKJHolo holo = HoloHandler.get().getHolo(key);
        if (holo == null)
            return;
        holo.delete();
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "hologram keyed as " + keyExpr.toString(event, debug);
    }

}
