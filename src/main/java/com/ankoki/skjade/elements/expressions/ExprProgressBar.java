package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.awt.*;

//i still dont want to touch this lmfao
/*@Name("Progress Bar")
@Description("Returns a progress bar with the specified colours. The default bar length/amount of bar characters is 50.")
@Examples("send title \"Smelting...\" with subtitle progress bar with the value loop-value out of a total value of 100")
@Since("insert version")*/
public class ExprProgressBar extends SimpleExpression<String> {
    private static final Color[] defaultColours = new Color[]{Color.GREEN, Color.GRAY};

    /*static {
        Skript.registerExpression(ExprProgressBar.class, String.class, ExpressionType.SIMPLE,
                "[a] [new] progress[ ]bar [(string|text|txt)] with [([the]|(current|filled))] value %number% [out] of [[a] total [value] [of]] %number% [(using|with) [the] [bar] char[acter] %-string%] [([and] (using|with) [the]|and) colo[u]rs %-colors%] [(1¦with %-number% bar char[acter]s)]");
    }*/

    private Expression<Number> currentValue, maxValue, barLength;
    private Expression<String> barCharacter;
    private Expression<Color> colours;

    @Override
    protected String[] get(Event e) {
        Color[] allColours;
        if (colours == null || colours.getSingle(e) == null) allColours = defaultColours;
        else allColours = colours.getArray(e);
        int max = maxValue.getSingle(e).intValue();
        int current = currentValue.getSingle(e).intValue();
        String bar = "|";
        if (barCharacter != null && barCharacter.getSingle(e) != null) bar = barCharacter.getSingle(e);
        if (current > max) current = max;
        int bl = 50;                             //yes i am fully aware that this is a mess let me live
        if (barLength != null) bl = barLength.getSingle(e).intValue();

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.of(allColours[0]));
        for (int i = 0; i < (bl + 1); i++) builder.append(bar);
        builder.insert(Math.max((bl / max) * current, 2), allColours.length < 2 ? "§7" : ChatColor.of(allColours[1]));
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
        if (barCharacter == null) colours = (Expression<Color>) exprs[2];
        else colours = (Expression<Color>) exprs[3];
        if (parseResult.mark == 1) barLength = (Expression<Number>) exprs[exprs.length - 1];
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