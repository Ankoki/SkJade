package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

//Ignore this class. Its a test as the HolographicDisplays API doesn't include the
//ImageMessage class, yet i still want to have a go to try and get it.
public class ExprImageLines extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprImageLines.class, String.class, ExpressionType.SIMPLE,
                "[hologram] lines of [the] image %string% with [(a|the)] width [of] %number%");
    }

    Expression<String> imageName;
    Expression<Number> widthExpr;

    @Nullable
    @Override
    protected String[] get(Event e) {
        String name = imageName.getSingle(e);
        Number number = widthExpr.getSingle(e);
        if (number == null || name == null) return new String[0];
        int width = number.intValue();
        try {
            Class clazz = Class.forName("com.gmail.filoghost.holographicdisplays.image.ImageMessage");
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance(ImageIO.read(new File(SkJade.getInstance().getDataFolder() + File.separator + name)), width);
            Field field = clazz.getDeclaredField("getLines");
            field.setAccessible(true);
            return ((List<String>) field.get(null)).toArray(new String[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new String[0];
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "lines of the image " + imageName.toString(e, debug) + " with width " + widthExpr.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        imageName = (Expression<String>) exprs[0];
        widthExpr = (Expression<Number>) exprs[1];
        return true;
    }
}
