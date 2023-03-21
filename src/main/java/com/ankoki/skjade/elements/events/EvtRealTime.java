package com.ankoki.skjade.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Time;
import com.ankoki.skjade.utils.events.RealTimeEvent;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Name("Real Time")
@Description("An event called at a real time, GMT time.")
@Examples("when it's 12:15 in the real world:")
@Since("1.1.0")
public class EvtRealTime extends SkriptEvent {
    private static final DateFormat format = new SimpleDateFormat("HH:mm");

    static {
        Skript.registerEvent("real time", EvtRealTime.class, RealTimeEvent.class,
                "(at|when it([']s| is)) %times% (server time|in (the real world|real life)|irl)");
    }

    private Expression<Time> timeExpr;

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        timeExpr = (Expression<Time>) args[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (event instanceof RealTimeEvent) {
            for (Time time : timeExpr.getArray(event)) {
                if (time.toString().equalsIgnoreCase(format.format(((RealTimeEvent) event).getDate()))) return true;
            }
        } return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "at " + timeExpr.toString(e, debug) + " in the real world";
    }

}
