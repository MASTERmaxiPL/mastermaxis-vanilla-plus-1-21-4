package net.mastermaxi.mastermaxisvanillaplus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InfectedWoodBlockEntity extends BlockEntity {
    private BlockState originalState = net.minecraft.block.Blocks.OAK_LOG.getDefaultState();
    private int lifeTicks = 500; // ~25 seconds

    public InfectedWoodBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.INFECTED_WOOD, pos, state);
    }

    public BlockState getOriginalState() {
        return this.originalState;
    }

    public void setOriginalState(BlockState state) {
        this.originalState = state;
        this.markDirty();
        if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public void setLifeTicks(int ticks) {
        this.lifeTicks = ticks;
        this.markDirty();
    }

    public static void tick(World world, BlockPos pos, BlockState state, InfectedWoodBlockEntity blockEntity) {
        if (!(world instanceof ServerWorld serverWorld) || !state.isOf(Blocks.INFECTED_WOOD)) {
            return;
        }

        blockEntity.lifeTicks--;

        // Occasional termite chewing sounds
        if (blockEntity.lifeTicks % 50 == 0) {
            world.playSound(null, pos, SoundEvents.ENTITY_SILVERFISH_STEP, SoundCategory.BLOCKS, 0.4F, 1.4F + (world.random.nextFloat() * 0.4F));
        }

        // Spread to adjacent logs/planks
        if (blockEntity.lifeTicks % 60 == 0) {
            for (Direction dir : Direction.values()) {
                BlockPos targetPos = pos.offset(dir);
                BlockState targetState = world.getBlockState(targetPos);
                if (InfectedMoundBlock.isInfectable(targetState)) {
                    InfectedMoundBlock.infect(serverWorld, targetPos, targetState, world.random);
                    break;
                }
            }
        }

        // When termite infection completes: destroy block and maybe spawn infected mound
        if (blockEntity.lifeTicks <= 0) {
            BlockState orig = blockEntity.originalState;
            world.playSound(null, pos, orig.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, orig),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 25, 0.3, 0.3, 0.3, 0.15);

            float chance = orig.isIn(BlockTags.LOGS) ? 0.40F : 0.30F;
            if (world.random.nextFloat() < chance) {
                world.setBlockState(pos, Blocks.INFECTED_MOUND.getDefaultState(), Block.NOTIFY_ALL);
            } else {
                world.setBlockState(pos, Blocks.MOUND.getDefaultState(), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        if (this.originalState != null) {
            nbt.put("OriginalState", NbtHelper.fromBlockState(this.originalState));
        }
        nbt.putInt("LifeTicks", this.lifeTicks);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (nbt.contains("OriginalState", NbtElement.COMPOUND_TYPE)) {
            this.originalState = NbtHelper.toBlockState(registries.getOrThrow(RegistryKeys.BLOCK), nbt.getCompound("OriginalState"));
        }
        this.lifeTicks = nbt.getInt("LifeTicks");
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return this.createNbt(registries);
    }
}
