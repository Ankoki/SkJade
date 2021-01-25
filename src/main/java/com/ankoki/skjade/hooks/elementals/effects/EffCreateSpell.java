package com.ankoki.skjade.hooks.elementals.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.function.ExprFunctionCall;
import ch.njol.skript.lang.function.FunctionReference;
import ch.njol.util.Kleenean;
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.api.EntitySpell;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.skjade.SkJade;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Register Elementals Spell")
@Description({"Registers a new elementals spell which can be cast.",
              "The spell name and id has to be unique, or the spell will not be created.",
              "IMPORTANT: The function has to return a boolean, and return true where you want the cooldown to be applied, and false where not."})
@Examples("register a new generic elementals spell named \"mySpell\" with the id 20284 to run the function mySpell()")
@RequiredPlugins("Elementals")
@Since("1.0")
public class EffCreateSpell extends Effect {

    static {
        Skript.registerEffect(EffCreateSpell.class,
                "(create|make|register) a[n] [new] (1¦generic|2¦entity) [elementals] spell named %string% with [the] id %number% to run [[the] function] [on cast] <(.+)>\\([<.*?>]\\) with [(a|the)] cooldown [of] %number% seconds");
    }

    private Expression<String> spellName;
    private Expression<Number> id;
    private Expression<Number> cooldown;
    private ExprFunctionCall functionCall;
    private boolean generic;

    @Override
    protected void execute(Event event) {
        String name = spellName.getSingle(event);
        int i = id.getSingle(event).intValue();
        int cool = cooldown.getSingle(event).intValue();
        if (name == null) return;
        Spell spell = new Spell(name, i, false);
        try {
            ElementalsAPI.registerSpell(SkJade.getInstance(), spell);
        } catch (Exception ex) {
            return;
        }
        if (generic) {
            ElementalsAPI.registerGenericSpells(SkJade.getInstance(), new GenericSpell() {
                @Override
                public boolean onCast(Player player) {
                    return (Boolean) functionCall.getAll(event)[0];
                }

                @Override
                public int getCooldown() {
                    return cool;
                }

                @Override
                public Spell getSpell() {
                    return spell;
                }
            });
            return;
        }
        ElementalsAPI.registerEntitySpells(SkJade.getInstance(), new EntitySpell() {
            @Override
            public boolean onCast(Player player, Entity entity) {
                return (Boolean) functionCall.getAll(event)[0];
            }

            @Override
            public int getCooldown() {
                return cool;
            }

            @Override
            public Spell getSpell() {
                return spell;
            }
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "create an elementals spell";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        spellName = (Expression<String>) exprs[0];
        id = (Expression<Number>) exprs[1];
        cooldown = (Expression<Number>) exprs[2];
        generic = parseResult.mark == 1;
        String unparsed = parseResult.regexes.get(0).group(0) + "(" + (parseResult.regexes.size() > 1 ? parseResult.regexes.get(1).group(0) : "") + ")";
        FunctionReference<?> function = new SkriptParser(unparsed, SkriptParser.ALL_FLAGS, ParseContext.DEFAULT)
                .parseFunction((Class<?>[]) null);
        if (function == null) {
            Skript.error("This isn't a valid function! Your function needs to return a value!");
            return false;
        }
        if (function.getReturnType() != Boolean.class) {
            Skript.error("You cannot have a return value when casting a spell!");
        }
        functionCall = new ExprFunctionCall(function);
        return true;
    }
}
