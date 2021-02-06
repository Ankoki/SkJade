package com.ankoki.skjade.elements.expressions;

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
import com.ankoki.skjade.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Progress Bar")
@Description("Returns a progress bar with the specified colours.")
@Examples("send title \"Smelting...\" with subtitle progress bar with the value loop-value out of a total value of 100 with bar char \"|\"")
@Since("1.0.0")
public class ExprProgressBar extends SimpleExpression<String> {
    private static final Color[] defaultColours = new Color[]{Color.GREEN, Color.GRAY};

    static {
        Skript.registerExpression(ExprProgressBar.class, String.class, ExpressionType.SIMPLE,
                "[a] [new] progress[ ]bar [(string|text|txt)] with [([the]|(current|filled))] value %number% [out] of [[a] total [value] [of]] %number% (using|with) [the] [bar] char[acter] %string% [([and] (using|with) [the]|and) colo[u]rs %-colors%]");
    }

    private Expression<Number> currentValue, maxValue;
    private Expression<String> barCharacter;
    private Expression<Color> colours;

    @Override
    protected String[] get(Event e) {
        Color[] allColours;
        if (colours == null || colours.getSingle(e) == null) allColours = defaultColours;
        else allColours = colours.getArray(e);
        int max = maxValue.getSingle(e).intValue();
        int current = currentValue.getSingle(e).intValue();
        String bar = barCharacter.getSingle(e);
        if (bar == null) bar = "|";

        StringBuilder builder = new StringBuilder();
        if (current > 0) builder.append(ChatColor.of(Utils.rgbToHex(allColours[0].getRed(), allColours[0].getGreen(), allColours[0].getBlue())));
        else if (allColours.length >= 2) builder.append(ChatColor.of(Utils.rgbToHex(allColours[1].getRed(), allColours[1].getGreen(), allColours[1].getBlue())));
        else builder.append("§7");

        for (int i = 0; i < max; i++) {
            builder.append(bar);
        }
        builder.insert(current, ChatColor.of(Utils.rgbToHex(allColours[0].getRed(), allColours[0].getGreen(), allColours[0].getBlue())));

        return new String[]{builder.toString()};
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "progress bar with the value " + currentValue.toString(e, debug) +
                " and max value " + maxValue.toString(e, debug) +
                " with the bar character " + barCharacter.toString(e, debug) +
                (colours == null ? "" : " and the colours " + colours.toString(e, debug));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        currentValue = (Expression<Number>) exprs[0];
        maxValue = (Expression<Number>) exprs[1];
        barCharacter = (Expression<String>) exprs[2];
        colours = (Expression<Color>) exprs[3];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}

/*
!send title "Smelting..." with subtitle (progress bar with the value 20 out of a total value of 45 with bar char "|") to player
 */