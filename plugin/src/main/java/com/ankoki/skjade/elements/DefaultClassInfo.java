package com.ankoki.skjade.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;

public class DefaultClassInfo {

    static {
        Classes.registerClass(new ClassInfo<>(Character.class, "Character")
        .user("char(acter)?s?")
        .name("Character")
        .description("A single character.")
        .since("insert version"));

        Converters.registerConverter(Character.class, String.class, String::valueOf);
        Converters.registerConverter(Character.class, Integer.class, Character::getNumericValue);
    }
}
