package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.util.Utils;
import org.eclipse.jdt.annotation.Nullable;

@Name("English Plural")
@Description("Returns the english plural of a word. This may be inaccurate.")
@Examples("set {_plural} to english plural of \"helicopter\"")
@Since("1.0.0")
public class ExprEnglishPlural extends SimplePropertyExpression<String, String> {

    static {
        Skript.registerExpression(ExprEnglishPlural.class, String.class, ExpressionType.PROPERTY,
                "[the] [english] plural of %string%");
    }

    @Override
    protected String getPropertyName() {
        return "english plural";
    }

    @Nullable
    @Override
    public String convert(String s) {
        return Utils.toEnglishPlural(s);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
