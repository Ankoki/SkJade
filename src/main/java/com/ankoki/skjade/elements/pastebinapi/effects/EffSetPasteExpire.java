package com.ankoki.skjade.elements.pastebinapi.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.pastebinapi.api.PasteBuilder;
import com.ankoki.pastebinapi.enums.PasteExpiry;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

@Name("Set Paste to Expire")
@Description("Set when a paste will expire.")
@Examples("set the paste with the id \"myPaste\" to never expire")
@Since("1.0.0")
public class EffSetPasteExpire extends Effect {

    static {
        Skript.registerEffect(EffSetPasteExpire.class,
                "set %pastes% to never expire",
                "set %pastes% to expire in (a|1|one) year",
                "set %pastes% to expire in (6|six) months",
                "set %pastes% to expire in (a|1|one) month",
                "set %pastes% to expire in (2|two) weeks",
                "set %pastes% to expire in (a|1|one) week",
                "set %pastes% to expire in (a|1|one) day",
                "set %pastes% to expire in (an|1|one) hour",
                "set %pastes% to expire in (ten|10) min[ute]s");
    }

    private Expression<PasteBuilder> paste;
    private PasteExpiry expires;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        switch (matchedPattern) {
            case 0:
                expires = PasteExpiry.NEVER;
                break;
            case 1:
                expires = PasteExpiry.ONE_YEAR;
                break;
            case 2:
                expires = PasteExpiry.SIX_MONTHS;
                break;
            case 3:
                expires = PasteExpiry.ONE_MONTH;
                break;
            case 4:
                expires = PasteExpiry.TWO_WEEKS;
                break;
            case 5:
                expires = PasteExpiry.ONE_WEEK;
                break;
            case 6:
                expires = PasteExpiry.ONE_DAY;
                break;
            case 7:
                expires = PasteExpiry.ONE_HOUR;
                break;
            case 8:
                expires = PasteExpiry.TEN_MINUTES;
        }
        paste = (Expression<PasteBuilder>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        PasteBuilder[] builder = paste.getArray(e);
        if (builder.length > 0 || expires != null) {
            Arrays.stream(builder).forEach(b -> PasteManager.setExpires(b, expires));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set " + paste.toString(e, debug) + " to expire";
    }
}
