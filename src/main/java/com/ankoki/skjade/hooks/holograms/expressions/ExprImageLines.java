package com.ankoki.skjade.hooks.holograms.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import io.netty.util.concurrent.CompleteFuture;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Name("Image Lines")
@Description("Returns all the lines to make up an image. Max width is 150 and the image should be in the SkJade plugin folder.")
@Examples("add line hologram lines of image \"urmum.png\" with a width of 30 to hologram with id \"test-holo\"")
@RequiredPlugins("HolographicDisplays")
@Since("1.3.0")
public class ExprImageLines extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprImageLines.class, String.class, ExpressionType.SIMPLE,
                "[hologram] lines of [the] image %string% with [(a|the)] width [of] %number%",
                "[hologram] lines of [the] image from [the] url %string% with [(a|the)] width [of] %number%");
    }

    private boolean fromUrl;
    private Expression<String> imageName;
    private Expression<Number> widthExpr;

    @Nullable
    @Override
    protected String[] get(Event e) {
        String name = imageName.getSingle(e);
        Number number = widthExpr.getSingle(e);
        if (number == null || name == null) return new String[0];
        int width = number.intValue();
        if (width > 150) return new String[0];
        try {
            Class clazz = Class.forName("com.gmail.filoghost.holographicdisplays.image.ImageMessage");
            Constructor<?> constructor = clazz.getDeclaredConstructor(BufferedImage.class, int.class);
            constructor.setAccessible(true);
            BufferedImage bufferedImage;
            if (fromUrl) {
                Delay.addDelayedEvent(e);
                CompletableFuture<BufferedImage> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        URL url = new URL(name);
                        return ImageIO.read(url);
                    } catch (Exception ignored) {}
                    return null;
                });
                while (!future.isDone()) {}
                bufferedImage = future.get();
            } else {
                File file = new File(SkJade.getInstance().getDataFolder() + File.separator + name);
                if (!file.exists() || file.isDirectory()) return new String[0];
                bufferedImage = ImageIO.read(file);
            }
            if (bufferedImage == null) return new String[0];
            Object object = constructor.newInstance(bufferedImage, width);
            Method method = clazz.getDeclaredMethod("getLines");
            method.setAccessible(true);
            return (String[]) method.invoke(object);
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
        fromUrl = matchedPattern == 1;
        return true;
    }
}
