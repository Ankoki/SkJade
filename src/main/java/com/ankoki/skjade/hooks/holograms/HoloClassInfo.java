package com.ankoki.skjade.hooks.holograms;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import ch.njol.util.coll.CollectionUtils;
import ch.njol.yggdrasil.Fields;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class HoloClassInfo {

    static {
        try {
            Classes.registerClass(new ClassInfo<>(Hologram.class, "hologram")
                    .user("holo(gram)?s?")
                    .name("Hologram")
                    .description("A Hologram created with Holographic displays.")
                    .since("1.0.0")
                    .changer(new Changer<Hologram>() {
                        @Nullable
                        @Override
                        public Class<?>[] acceptChange(ChangeMode mode) {
                            if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET || mode == ChangeMode.ADD) {
                                return CollectionUtils.array(String.class, ItemStack.class);
                            }
                            return null;
                        }

                        @Override
                        public void change(Hologram[] holograms, @Nullable Object[] delta, ChangeMode mode) {
                            if (mode == ChangeMode.DELETE) {
                                HologramManager.deleteHologram(holograms);
                            } else if (mode == ChangeMode.ADD) {
                                if (delta == null || delta[0] == null) return;
                                if (delta[0] instanceof String) {
                                    for (Hologram hologram : holograms) {
                                        HologramManager.addTextLine(hologram, (String) delta[0]);
                                    }
                                } else if (delta[0] instanceof ItemStack) {
                                    for (Hologram hologram : holograms) {
                                        HologramManager.addItemLine(hologram, (ItemStack) delta[0]);
                                    }
                                }
                            } else {
                                for (Hologram hologram : holograms) {
                                    if (!hologram.isDeleted()) {
                                        hologram.clearLines();
                                    }
                                }
                            }
                        }
                    })
                    .parser(new Parser<Hologram>() {
                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public String toString(Hologram hologram, int i) {
                            return "hologram";
                        }

                        @Override
                        public String toVariableNameString(Hologram hologram) {
                            return "hologram:" + hologram.getCreationTimestamp();
                        }
                    })
                    .serializer(new Serializer<Hologram>() {
                        @Override
                        public Fields serialize(Hologram hologram) throws NotSerializableException {
                            Fields fields = new Fields();
                            fields.putObject("id", HologramManager.getIDFromHolo(hologram));
                            fields.putObject("location", HologramManager.getHoloLocation(hologram));
                            fields.putObject("lines", HologramManager.getLines(hologram));

                            return fields;
                        }

                        @Override
                        public void deserialize(Hologram hologram, Fields fields) throws StreamCorruptedException, NotSerializableException {
                            if (!hologram.isDeleted()) hologram.delete();
                            HologramManager.createHologram((String) fields.getObject("id"),
                                    (Location) fields.getObject("location"), true, true);
                        }

                        @Override
                        public boolean mustSyncDeserialization() {
                            return true;
                        }

                        @Override
                        protected boolean canBeInstantiated() {
                            return false;
                        }
                    }));

            Converters.registerConverter(Hologram.class, Location.class, Hologram::getLocation);

            Classes.registerClass(new ClassInfo<>(HologramLine.class, "hologramline")
                    .user("holo(gram)?( |-)?lines?")
                    .name("Hologram Line")
                    .description("A line of a Hologram.")
                    .since("1.0.0")
                    .changer(new Changer<HologramLine>() {
                        @Nullable
                        @Override
                        public Class<?>[] acceptChange(ChangeMode mode) {
                            if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
                                return CollectionUtils.array();
                            }
                            return null;
                        }

                        @Override
                        public void change(HologramLine[] hologramLines, @Nullable Object[] objects, ChangeMode mode) {
                            for (HologramLine line : hologramLines) {
                                line.removeLine();
                            }
                        }
                    })
                    .parser(new Parser<HologramLine>() {
                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public String toString(HologramLine hologramLine, int i) {
                            return hologramLine instanceof ItemLine ? "hologram item line" : "hologram text line";
                        }

                        @Override
                        public String toVariableNameString(HologramLine hologramLine) {
                            return hologramLine instanceof ItemLine ? "hologram item line" : "hologram text line";
                        }
                    }));

            Converters.registerConverter(TextLine.class, String.class, TextLine::getText);
            Converters.registerConverter(ItemLine.class, ItemType.class, line -> new ItemType(line.getItemStack()));
            Converters.registerConverter(HologramLine.class, Number.class, line -> {
                Hologram parent = line.getParent();
                for (int i = 0; i < parent.size(); i++) {
                    if (parent.getLine(i).equals(line)) return i++;
                }
                return null;
            });
        } catch(IllegalArgumentException ignored) {}
    }
}
