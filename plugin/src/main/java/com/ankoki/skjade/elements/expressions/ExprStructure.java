package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.StructureType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Structure")
@Description("All valid structure types.")
@Examples("stronghold")
@Since("1.3.0")
public class ExprStructure extends SimpleExpression<StructureType> {

    static {
        Skript.registerExpression(ExprStructure.class, StructureType.class, ExpressionType.SIMPLE,
                "bastion[ remenant]",
                "[buried ]treasure",
                "desert (pyramid|temple)",
                "end city",
                "igloo",
                "jungle (pyramid|temple)",
                "mineshaft",
                "nether fortress",
                "nether fossil",
                "ocean monument",
                "ocean ruin",
                "pillager outpost",
                "ruined portal",
                "shipwreck",
                "stronghold",
                "(swamp|witch[es]) hut",
                "village",
                "[woodland ]mansion");
    }

    private int pattern;

    @Nullable
    @Override
    protected StructureType[] get(Event e) {
        StructureType type = null;
        switch (pattern) {
            case 0:
                type = StructureType.BASTION_REMNANT;
                break;
            case 1:
                type = StructureType.BURIED_TREASURE;
                break;
            case 2:
                type = StructureType.DESERT_PYRAMID;
                break;
            case 3:
                type = StructureType.END_CITY;
                break;
            case 4:
                type = StructureType.IGLOO;
                break;
            case 5:
                type = StructureType.JUNGLE_PYRAMID;
                break;
            case 6:
                type = StructureType.MINESHAFT;
                break;
            case 7:
                type = StructureType.NETHER_FORTRESS;
                break;
            case 8:
                type = StructureType.NETHER_FOSSIL;
                break;
            case 9:
                type = StructureType.OCEAN_MONUMENT;
                break;
            case 10:
                type = StructureType.OCEAN_RUIN;
                break;
            case 11:
                type = StructureType.PILLAGER_OUTPOST;
                break;
            case 12:
                type = StructureType.RUINED_PORTAL;
                break;
            case 13:
                type = StructureType.SHIPWRECK;
                break;
            case 14:
                type = StructureType.STRONGHOLD;
                break;
            case 15:
                type = StructureType.SWAMP_HUT;
                break;
            case 16:
                type = StructureType.VILLAGE;
                break;
            case 17:
                type = StructureType.WOODLAND_MANSION;
                break;
        }
        return type == null ? new StructureType[0] : new StructureType[]{type};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends StructureType> getReturnType() {
        return StructureType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        switch (pattern) {
            case 0:
                return "bastion";
            case 1:
                return "buried treasure";
            case 2:
                return "desert temple";
            case 3:
                return "end city";
            case 4:
                return "igloo";
            case 5:
                return "jungle temple";
            case 6:
                return "mineshaft";
            case 7:
                return "nether fortress";
            case 8:
                return "nether fossil";
            case 9:
                return "ocean monument";
            case 10:
                return "ocean ruin";
            case 11:
                return "pillager outpost";
            case 12:
                return "ruined portal";
            case 13:
                return "shipwreck";
            case 14:
                return "stronghold";
            case 15:
                return "witch hut";
            case 16:
                return "village";
            case 17:
                return "woodland mansion";
        }
        return "structure type";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pattern = matchedPattern;
        return true;
    }
}
