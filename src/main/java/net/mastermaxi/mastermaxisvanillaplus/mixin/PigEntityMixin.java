package net.mastermaxi.mastermaxisvanillaplus.mixin;

import net.mastermaxi.mastermaxisvanillaplus.entity.ai.PigDigTruffleGoal;
import net.mastermaxi.mastermaxisvanillaplus.entity.ai.PigEatGrassGoal;
import net.mastermaxi.mastermaxisvanillaplus.entity.ai.PigWanderToMudGoal;
import net.mastermaxi.mastermaxisvanillaplus.entity.custom.MuddyPigAccess;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity implements MuddyPigAccess {
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Integer> MUD_VARIANT = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Boolean> IS_DIGGING = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Boolean> IS_WALLOWING = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private int mastermaxisvanillaplus$happiness = 30;
    @Unique
    private int mastermaxisvanillaplus$wallowTicks = 0;
    @Unique
    private int mastermaxisvanillaplus$washTicks = 0;
    @Unique
    private int mastermaxisvanillaplus$mudTimer = 0;
    @Unique
    private int mastermaxisvanillaplus$decayTimer = 0;

    protected PigEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void mastermaxisvanillaplus$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(MUD_VARIANT, 0);
        builder.add(IS_DIGGING, false);
        builder.add(IS_WALLOWING, false);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void mastermaxisvanillaplus$initGoals(CallbackInfo ci) {
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 0.0F);

        PigEntity pig = (PigEntity) (Object) this;
        this.goalSelector.add(4, new PigDigTruffleGoal(pig));
        this.goalSelector.add(5, new PigEatGrassGoal(pig));
        this.goalSelector.add(6, new PigWanderToMudGoal(pig, 1.0));
    }

    @Override
    public void mastermaxisvanillaplus$tickPig() {
        if (this.getWorld().isClient()) {
            return;
        }

        boolean inMud = mastermaxisvanillaplus$isInMudFluid();

        if (inMud) {
            mastermaxisvanillaplus$washTicks = 0;
            mastermaxisvanillaplus$wallowTicks++;
            mastermaxisvanillaplus$mudTimer = 6000; // 5 minutes mud duration

            if (!mastermaxisvanillaplus$isWallowing() && mastermaxisvanillaplus$wallowTicks > 0) {
                mastermaxisvanillaplus$setWallowing(true);
                this.getNavigation().stop();
            }

            if (this.age % 20 == 0) {
                mastermaxisvanillaplus$addHappiness(5);
            }

            if (this.random.nextInt(4) == 0) {
                ((ServerWorld) this.getWorld()).spawnParticles(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.MUD.getDefaultState()),
                        this.getX() + (this.random.nextDouble() - 0.5) * 0.6,
                        this.getY() + 0.2,
                        this.getZ() + (this.random.nextDouble() - 0.5) * 0.6,
                        5, 0.2, 0.1, 0.2, 0.08
                );
                if (this.random.nextInt(2) == 0) {
                    this.playSound(SoundEvents.ENTITY_GENERIC_SPLASH, 0.6F, 0.8F + this.random.nextFloat() * 0.3F);
                }
            }

            if (this.random.nextInt(30) == 0) {
                this.playSound(SoundEvents.ENTITY_PIG_AMBIENT, 0.9F, 1.2F);
            }

            if (mastermaxisvanillaplus$getMudVariant() == 0 && mastermaxisvanillaplus$wallowTicks >= 60) {
                int chosen = this.random.nextBoolean() ? 1 : 2;
                mastermaxisvanillaplus$setMudVariant(chosen);
                mastermaxisvanillaplus$addHappiness(30);
                this.playSound(SoundEvents.ENTITY_PIG_AMBIENT, 1.0F, 1.2F);
                this.playSound(SoundEvents.ENTITY_GENERIC_SPLASH, 0.8F, 1.0F);

                ((ServerWorld) this.getWorld()).spawnParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        this.getX(), this.getY() + 0.6, this.getZ(),
                        8, 0.3, 0.2, 0.3, 0.0
                );
            }

            if (mastermaxisvanillaplus$wallowTicks >= 80) {
                mastermaxisvanillaplus$setWallowing(false);
                mastermaxisvanillaplus$wallowTicks = -100; // 5s cooldown
            }
        } else {
            if (mastermaxisvanillaplus$isWallowing()) {
                mastermaxisvanillaplus$setWallowing(false);
            }
            if (mastermaxisvanillaplus$wallowTicks > 0) {
                mastermaxisvanillaplus$wallowTicks = 0;
            } else if (mastermaxisvanillaplus$wallowTicks < 0) {
                mastermaxisvanillaplus$wallowTicks++;
            }

            if (mastermaxisvanillaplus$getMudVariant() > 0) {
                if (this.isTouchingWater()) {
                    mastermaxisvanillaplus$washTicks++;
                    if (mastermaxisvanillaplus$washTicks >= 80) {
                        mastermaxisvanillaplus$cleanPig();
                    }
                } else if (this.getWorld().hasRain(this.getBlockPos())) {
                    mastermaxisvanillaplus$washTicks++;
                    if (mastermaxisvanillaplus$washTicks >= 400) {
                        mastermaxisvanillaplus$cleanPig();
                    }
                } else {
                    mastermaxisvanillaplus$washTicks = 0;
                    mastermaxisvanillaplus$mudTimer--;
                    if (mastermaxisvanillaplus$mudTimer <= 0) {
                        mastermaxisvanillaplus$dryMudPig();
                    }
                }
            }
        }

        mastermaxisvanillaplus$decayTimer++;
        if (mastermaxisvanillaplus$decayTimer >= 400) {
            mastermaxisvanillaplus$decayTimer = 0;
            if (mastermaxisvanillaplus$happiness > 20) {
                mastermaxisvanillaplus$happiness--;
            }
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"))
    private void mastermaxisvanillaplus$interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.GOLDEN_CARROT)) {
            mastermaxisvanillaplus$addHappiness(40);
        } else if (stack.isOf(Items.CARROT) || stack.isOf(Items.POTATO) || stack.isOf(Items.BEETROOT)) {
            mastermaxisvanillaplus$addHappiness(20);
        }
    }

    @Unique
    private boolean mastermaxisvanillaplus$isInMudFluid() {
        Box box = this.getBoundingBox().expand(0.0, 0.1, 0.0);
        int minX = MathHelper.floor(box.minX);
        int maxX = MathHelper.ceil(box.maxX);
        int minY = MathHelper.floor(box.minY - 0.2);
        int maxY = MathHelper.ceil(box.maxY);
        int minZ = MathHelper.floor(box.minZ);
        int maxZ = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    mutable.set(x, y, z);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.getFluid() == FluidInit.MUD_WATER_STILL || fluidState.getFluid() == FluidInit.MUD_WATER_FLOWING) {
                        return true;
                    }
                    if (this.getWorld().getBlockState(mutable).isOf(FluidInit.MUD_WATER_BLOCK)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private void mastermaxisvanillaplus$cleanPig() {
        mastermaxisvanillaplus$setMudVariant(0);
        mastermaxisvanillaplus$washTicks = 0;
        mastermaxisvanillaplus$mudTimer = 0;
        this.playSound(SoundEvents.ENTITY_GENERIC_SPLASH, 0.8F, 1.2F);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.SPLASH,
                    this.getX(), this.getY() + 0.5, this.getZ(),
                    12, 0.3, 0.2, 0.3, 0.1
            );
        }
    }

    @Unique
    private void mastermaxisvanillaplus$dryMudPig() {
        mastermaxisvanillaplus$setMudVariant(0);
        mastermaxisvanillaplus$washTicks = 0;
        mastermaxisvanillaplus$mudTimer = 0;
        this.playSound(SoundEvents.BLOCK_GRAVEL_BREAK, 0.8F, 0.9F);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.MUD.getDefaultState()),
                    this.getX(), this.getY() + 0.4, this.getZ(),
                    12, 0.3, 0.2, 0.3, 0.05
            );
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void mastermaxisvanillaplus$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("MudVariant", mastermaxisvanillaplus$getMudVariant());
        nbt.putInt("Happiness", mastermaxisvanillaplus$happiness);
        nbt.putInt("MudTimer", mastermaxisvanillaplus$mudTimer);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void mastermaxisvanillaplus$readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("MudVariant")) {
            mastermaxisvanillaplus$setMudVariant(nbt.getInt("MudVariant"));
        }
        if (nbt.contains("Happiness")) {
            mastermaxisvanillaplus$happiness = nbt.getInt("Happiness");
        }
        if (nbt.contains("MudTimer")) {
            mastermaxisvanillaplus$mudTimer = nbt.getInt("MudTimer");
        }
    }

    @Override
    public int mastermaxisvanillaplus$getMudVariant() {
        return this.dataTracker.get(MUD_VARIANT);
    }

    @Override
    public void mastermaxisvanillaplus$setMudVariant(int variant) {
        this.dataTracker.set(MUD_VARIANT, variant);
        if (variant > 0 && this.mastermaxisvanillaplus$mudTimer <= 0) {
            this.mastermaxisvanillaplus$mudTimer = 6000;
        } else if (variant == 0) {
            this.mastermaxisvanillaplus$mudTimer = 0;
        }
    }

    @Override
    public int mastermaxisvanillaplus$getHappiness() {
        return this.mastermaxisvanillaplus$happiness;
    }

    @Override
    public void mastermaxisvanillaplus$setHappiness(int happiness) {
        this.mastermaxisvanillaplus$happiness = Math.max(0, Math.min(100, happiness));
    }

    @Override
    public void mastermaxisvanillaplus$addHappiness(int amount) {
        mastermaxisvanillaplus$setHappiness(this.mastermaxisvanillaplus$happiness + amount);
    }

    @Override
    public boolean mastermaxisvanillaplus$isDigging() {
        return this.dataTracker.get(IS_DIGGING);
    }

    @Override
    public void mastermaxisvanillaplus$setDigging(boolean digging) {
        this.dataTracker.set(IS_DIGGING, digging);
    }

    @Override
    public boolean mastermaxisvanillaplus$isWallowing() {
        return this.dataTracker.get(IS_WALLOWING);
    }

    @Override
    public void mastermaxisvanillaplus$setWallowing(boolean wallowing) {
        this.dataTracker.set(IS_WALLOWING, wallowing);
    }
}
