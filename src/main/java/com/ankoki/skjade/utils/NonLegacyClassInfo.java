package com.ankoki.skjade.utils;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.bukkit.StructureType;
import org.jetbrains.annotations.Nullable;

public class NonLegacyClassInfo {

    public NonLegacyClassInfo() {
        //StructureType ClassInfo
        Classes.registerClass(new ClassInfo<>(StructureType.class, "skjstructuretype")
                .user("skjstructuretype?s?")
                .name("Structure Type")
                .description("A specified structure type.")
                .since("1.3.0")
                .parser(new Parser<StructureType>() {
                    @Nullable
                    @Override
                    public StructureType parse(String s, ParseContext context) {
                        return StructureType.getStructureTypes().get(s);
                    }

                    @Override
                    public String toString(StructureType o, int flags) {
                        return o.getName().replace("_", " ");
                    }

                    @Override
                    public String toVariableNameString(StructureType o) {
                        return o.getName().toLowerCase().replace("_", " ");
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "[a-z ]+";
                    }
                }));
    }
}
