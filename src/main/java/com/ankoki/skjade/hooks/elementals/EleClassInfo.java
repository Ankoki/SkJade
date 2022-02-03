package com.ankoki.skjade.hooks.elementals;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import com.ankoki.elementals.managers.Spell;

public class EleClassInfo {

    static {
        Classes.registerClass(new ClassInfo<>(Spell.class, "spell")
        .user("spell?s?")
        .name("Spell")
        .description("An elementals spell")
        .since("1.0")
        .parser(new Parser<Spell>() {
            @Override
            public boolean canParse(ParseContext context) {
                return false;
            }

            @Override
            public String toString(Spell spell, int i) {
                return spell.getSpellName().toLowerCase();
            }

            @Override
            public String toVariableNameString(Spell spell) {
                return spell.getSpellName().toLowerCase();
            }

            public String getVariableNamePattern() {
                return "\\S+";
            }
        }));

        Converters.registerConverter(Spell.class, String.class, Spell::getSpellName);
    }
}
