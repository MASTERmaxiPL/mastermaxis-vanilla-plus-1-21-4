package net.mastermaxi.mastermaxisvanillaplus.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class BaobabLeavesBlock extends LeavesBlock implements Fertilizable {
    public static final MapCodec<BaobabLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createSettingsCodec(),
                    Codec.INT.fieldOf("stage").forGetter(BaobabLeavesBlock::getStage)
            ).apply(instance, BaobabLeavesBlock::new)
    );

    private final int stage;

    public BaobabLeavesBlock(Settings settings, int stage) {
        super(settings);
        this.stage = stage;
    }

    @Override
    public MapCodec<BaobabLeavesBlock> getCodec() {
        return CODEC;
    }

    public int getStage() {
        return this.stage;
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return this.stage < 2 || super.hasRandomTicks(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (world.getBlockState(pos).isAir()) {
            return;
        }

        if (this.stage == 0 && random.nextInt(20) == 0) {
            BlockState nextState = Blocks.BAOBAB_LEAVES_FLOWERS.getDefaultState()
                    .with(DISTANCE, state.get(DISTANCE))
                    .with(PERSISTENT, state.get(PERSISTENT))
                    .with(WATERLOGGED, state.get(WATERLOGGED));
            world.setBlockState(pos, nextState, Block.NOTIFY_LISTENERS);
        } else if (this.stage == 1 && random.nextInt(40) == 0) {
            BlockState nextState = Blocks.BAOBAB_LEAVES_FRUIT.getDefaultState()
                    .with(DISTANCE, state.get(DISTANCE))
                    .with(PERSISTENT, state.get(PERSISTENT))
                    .with(WATERLOGGED, state.get(WATERLOGGED));
            world.setBlockState(pos, nextState, Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (this.stage < 2 && stack.isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.stage == 2) {
            dropStack(world, pos, new ItemStack(ItemInit.BAOBAB_FRUIT, 1));
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            BlockState resetState = Blocks.BAOBAB_LEAVES.getDefaultState()
                    .with(DISTANCE, state.get(DISTANCE))
                    .with(PERSISTENT, state.get(PERSISTENT))
                    .with(WATERLOGGED, state.get(WATERLOGGED));
            world.setBlockState(pos, resetState, Block.NOTIFY_LISTENERS);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return this.stage < 2;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (this.stage < 2) {
            Block nextBlock = (this.stage == 0) ? Blocks.BAOBAB_LEAVES_FLOWERS : Blocks.BAOBAB_LEAVES_FRUIT;
            BlockState nextState = nextBlock.getDefaultState()
                    .with(DISTANCE, state.get(DISTANCE))
                    .with(PERSISTENT, state.get(PERSISTENT))
                    .with(WATERLOGGED, state.get(WATERLOGGED));
            world.setBlockState(pos, nextState, Block.NOTIFY_LISTENERS);
        }
    }
}
