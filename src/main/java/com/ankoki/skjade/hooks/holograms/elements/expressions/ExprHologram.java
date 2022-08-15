package com.ankoki.skjade.hooks.holograms.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.HoloManager;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram")
@Description("Gets a hologram via its key.")
@Examples("set {_holo} to holo keyed as \"so bad by stayc\"")
@Since("2.0")
public class ExprHologram extends SimpleExpression<SKJHolo> {

    static {
        Skript.registerExpression(ExprHologram.class, SKJHolo.class, ExpressionType.SIMPLE,
                "[the] [skjade] holo[gram] (key|nam)ed [as] %string%");
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
        if (key == null) return new SKJHolo[0];
        return new SKJHolo[]{HoloManager.get().getHolo(key)};
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
    public String toString(@Nullable Event event, boolean b) {
        return null;
    }
}
