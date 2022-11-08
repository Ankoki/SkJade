package com.ankoki.skjade.hooks.holograms.elements.effects.sub;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import com.ankoki.skjade.hooks.holograms.elements.sections.SecKeyedHologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Visibility")
@Description("Hides or shows the hologram to certain players." +
        "The first pattern must be used within a hologram creation section.")
@Examples({"create new holo keyed \"stacyGURLS!\":",
        "\tpage 0: \"it's going\", \"DOWWWWWN\", glowing diamond sword and an ender dragon",
        "\tpersistent: false",
        "\tstatic: true",
        "\thide from: event-player"})
@Since("2.0")
@RequiredPlugins("DecentHolograms/Holographic Displays")
public class EffVisibility extends Effect {


    static {
        if (HoloHandler.get().getCurrentProvider().supportsPerPlayer()) {
            if (HoloHandler.get().getCurrentProvider().supportsPages())
                Skript.registerEffect(EffVisibility.class,
                        "(show:show[ page %-number%] to|hide (for|from))\\: %players%",
                        "(show:show[ page %-number%] [of]|hide ) %skjholo% (to|for|from) %players%");
            else
                Skript.registerEffect(EffVisibility.class,
                        "(show:show to|hide (for|from))\\: %players%",
                        "(show:show|hide) %skjholo% (to|for|from) %players%");
        }
    }

    private boolean show;
    private boolean isSec;
    private Expression<Integer> pageExpr;
    private Expression<SKJHolo> holoExpr;
    private Expression<Player> playerExpr;

    private SecKeyedHologram section;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        isSec = i == 0;
        show = parseResult.hasTag("show");
        if (isSec) {
            SkriptEvent event = getParser().getCurrentSkriptEvent();
            if (event instanceof SectionSkriptEvent skriptEvent && skriptEvent.isSection(SecKeyedHologram.class)) {
                isSec = true;
                section = (SecKeyedHologram) skriptEvent.getSection();
                pageExpr = (Expression<Integer>) exprs[0];
                playerExpr = (Expression<Player>) exprs[1];
                return true;
            }
            Skript.error("You cannot edit a hologram outside of a section using this pattern.");
            return false;
        } else {
            pageExpr = (Expression<Integer>) exprs[0];
            holoExpr = (Expression<SKJHolo>) exprs[1];
            playerExpr = (Expression<Player>) exprs[2];
            return true;
        }
    }

    @Override
    protected void execute(Event event) {
        Player[] players = playerExpr.getAll(event);
        if (players.length == 0)
            return;
        int page = 0;
        if (pageExpr != null) {
            Number number = pageExpr.getSingle(event);
            if (number == null)
                return;
            page = number.intValue();
        }
        if (isSec) {
            SKJHoloBuilder builder = section.getCurrentBuilder();
            if (show)
                builder.showTo(page, players);
            else
                builder.hideFrom(players);
            section.setCurrentBuilder(builder);
        } else {
            SKJHolo holo = holoExpr.getSingle(event);
            if (holo == null)
                return;
            if (show)
                holo.showTo(page, players);
            else
                holo.hideFrom(players);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return (show ? "show " + (pageExpr == null ? "" : "page " + pageExpr.toString(event, b) + " ") + "to: " : "hide from: ") + playerExpr.toString(event, b);
    }
}
