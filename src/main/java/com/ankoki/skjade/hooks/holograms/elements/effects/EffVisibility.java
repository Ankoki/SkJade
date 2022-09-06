package com.ankoki.skjade.hooks.holograms.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SectionSkriptEvent;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.HoloManager;
import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloBuilder;
import com.ankoki.skjade.hooks.holograms.elements.sections.SecKeyedHologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Hologram Visibility")
@Description("Sets whether players can see or hide ")
@Examples("show page 2 of holo keyed as \"oreo fudge\" for all players")
@Since("2.0")
@RequiredPlugins("DecentHolograms")
public class EffVisibility extends Effect {

    static {
        HoloProvider provider = HoloManager.get().getCurrentProvider();
        if (provider.supportsPerPlayer()) Skript.registerEffect(EffVisibility.class,
                "(show:show[ page %-number%] to|hide:hide for)\\: %players%",
                "(show:show[ page %-number%] to|hide:hide ) %skjholo% for %players%");
        else
            SkJade.getInstance().getLogger().warning("Please note your current hologram provider (" + provider.getId() + ") " +
                    "does not support the use of per player holograms! You cannot use the visibility effect.");
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
            Skript.error("You cannot add lines or pages to a hologram outside of a section using this pattern.");
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
        if (players.length == 0) return;
        int page;
        if (pageExpr != null) {
            Integer p = pageExpr.getSingle(event);
            if (p == null) return;
            page = p;
        } else page = 0;
        if (isSec) {
            SKJHoloBuilder builder = section.getCurrentBuilder();
            if (show) builder.showTo(page, players);
            else builder.hideFrom(players);
            section.setCurrentBuilder(builder);
        } else {
            SKJHolo holo = holoExpr.getSingle(event);
            if (holo == null) return;
            if (show) holo.showTo(page, players);
            else holo.hideFrom(players);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return (show ? "show " + (pageExpr == null ? "" : "page " + pageExpr.toString(event, b) + " ") + "to: " : "hide from: ") + playerExpr.toString(event, b);
    }
}
