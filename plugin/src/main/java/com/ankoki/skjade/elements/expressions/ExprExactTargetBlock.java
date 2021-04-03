package com.ankoki.skjade.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptConfig;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Exact Target Block")
@Description("Gets the exact target block of player.")
@Examples("set {-target::%player's uuid%} to player's exact target block")
@Since("1.1.0")
public class ExprExactTargetBlock extends SimpleExpression<Block> {

    static {
        Skript.registerExpression(ExprExactTargetBlock.class, Block.class, ExpressionType.SIMPLE,
                "%player%'s exact target[ed] block",
                "%player%'s exact target[ed] block including (1¦[only ]source|[any] [type of]) fluid[s]");
    }

    private Expression<Player> player;
    private FluidCollisionMode mode = FluidCollisionMode.NEVER;

    @Nullable
    @Override
    protected Block[] get(Event e) {
        if (player == null) return null;
        Player p = player.getSingle(e);
        if (p == null) return null;
        return new Block[]{p.getTargetBlockExact(SkriptConfig.maxTargetBlockDistance.value(), mode)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Block> getReturnType() {
        return Block.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return player.toString(e, debug) + "'s exact target block";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean isDelayed, ParseResult parseResult) {
        if (i == 1 && parseResult.mark == 1) {
            mode = FluidCollisionMode.SOURCE_ONLY;
        } else if (i == 1) {
            mode = FluidCollisionMode.ALWAYS;
        }
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(ChangeMode mode) {
        return mode == ChangeMode.SET ? CollectionUtils.array(ItemType.class) : null;
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
        if (mode == ChangeMode.SET && delta != null && delta.length >= 1 && delta[0] instanceof ItemType) {
            ItemType item = (ItemType) delta[0];
            Block[] target = get(e);
            if (target.length < 1) return;
            Block block = target[0];
            if (block == null) return;
            block.setType(item.getMaterial());
        }
    }
}
