package com.ankoki.skjade.elements.pastebinapi;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.pastebinapi.api.PasteBuilder;
import org.jetbrains.annotations.Nullable;

public class PasteClassInfo {

    static {
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
    }
}
