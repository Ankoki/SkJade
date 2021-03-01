package com.ankoki.skjade.elements;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.pastebinapi.api.PasteBuilder;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import org.jetbrains.annotations.Nullable;

public class DefaultClassInfo {

    static {
        //Pastebin ClassInfo
        Classes.registerClass(new ClassInfo<>(PasteBuilder.class, "paste")
                .user("paste?s?")
                .name("Paste")
                .description("A PasteBuilder created with SkJade.")
                .since("1.0.0")
                .changer(new Changer<PasteBuilder>() {
                    @Nullable
                    @Override
                    public Class<?>[] acceptChange(ChangeMode mode) {
                        if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET || mode == ChangeMode.REMOVE_ALL) {
                            return CollectionUtils.array();
                        }
                        return null;
                    }

                    @Override
                    public void change(PasteBuilder[] what, @Nullable Object[] delta, ChangeMode mode) {
                        switch (mode) {
                            case DELETE:
                                PasteManager.deletePaste(what);
                                break;
                            case RESET:
                            case REMOVE_ALL:
                                PasteManager.resetPaste(what);
                        }
                    }
                }));

        //Character ClassInfo
        Classes.registerClass(new ClassInfo<>(Character.class, "character")
        .user("char(acter)?s?")
        .name("Character")
        .description("A single character.")
        .since("1.1.0"));

        Converters.registerConverter(Character.class, String.class, String::valueOf);
        Converters.registerConverter(Character.class, Integer.class, Character::getNumericValue);
    }
}
