package net.mastermaxi.mastermaxisvanillaplus.entity.custom;

import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.fluid.MudWaterFluid;
import net.mastermaxi.mastermaxisvanillaplus.item.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OstrichEntity extends AnimalEntity implements Angerable, Saddleable, JumpingMount {
    private static final TrackedData<Boolean> TAMED = DataTracker.registerData(OstrichEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SADDLED = DataTracker.registerData(OstrichEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> OSTRICH_SPRINTING = DataTracker.registerData(OstrichEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SPRINT_TICKS = DataTracker.registerData(OstrichEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> COOLDOWN_TICKS = DataTracker.registerData(OstrichEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState getHitAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public int idleAnimationId = 0;
    private int idleAnimationDuration = 0;
    private int idleAnimationCooldown = 0;
    private int attackTicksLeft;

    public int eggLayTime = this.random.nextInt(6000) + 6000;

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;

    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(10, 20);
    private int angerTime;
    @Nullable
    private UUID angryAt;

    public OstrichEntity(EntityType<? extends OstrichEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TAMED, false);
        builder.add(SADDLED, false);
        builder.add(OSTRICH_SPRINTING, false);
        builder.add(SPRINT_TICKS, 0);
        builder.add(COOLDOWN_TICKS, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 0.6, false));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.15D));
        this.goalSelector.add(3, new net.mastermaxi.mastermaxisvanillaplus.entity.ai.EatTermitesGoal(this, 1.15D, 16));
        this.goalSelector.add(4, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.GLISTERING_MELON_SLICE, Items.MELON_SLICE), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(3, new UniversalAngerGoal<>(this, false));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.MOVEMENT_SPEED, 0.24)
                .add(EntityAttributes.MAX_HEALTH, 12.0)
                .add(EntityAttributes.ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 1.0)
                .add(EntityAttributes.ATTACK_SPEED, 2)
                .add(EntityAttributes.FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.TEMPT_RANGE, 12.0)
                .add(EntityAttributes.STEP_HEIGHT, 1.0);
    }

    public boolean isTamed() {
        return this.dataTracker.get(TAMED);
    }

    public void setTamed(boolean tamed) {
        this.dataTracker.set(TAMED, tamed);
    }

    public boolean isSaddled() {
        return this.dataTracker.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.dataTracker.set(SADDLED, saddled);
    }

    public boolean isOstrichSprinting() {
        return this.dataTracker.get(OSTRICH_SPRINTING);
    }

    public void setOstrichSprinting(boolean sprinting) {
        this.dataTracker.set(OSTRICH_SPRINTING, sprinting);
    }

    public int getSprintTicks() {
        return this.dataTracker.get(SPRINT_TICKS);
    }

    public void setSprintTicks(int ticks) {
        this.dataTracker.set(SPRINT_TICKS, ticks);
    }

    public int getCooldownTicks() {
        return this.dataTracker.get(COOLDOWN_TICKS);
    }

    public void setCooldownTicks(int ticks) {
        this.dataTracker.set(COOLDOWN_TICKS, ticks);
    }

    public boolean canSprint() {
        return this.getSprintTicks() == 0 && this.getCooldownTicks() == 0;
    }

    public void startSprint() {
        this.setSprintTicks(200);
        this.setCooldownTicks(0);
        this.setOstrichSprinting(true);
        this.playSound(SoundEvents.ENTITY_CAMEL_DASH, 1.0F, 1.2F);
    }

    @Override
    public boolean canBeSaddled() {
        return this.isAlive() && !this.isBaby() && this.isTamed();
    }

    @Override
    public void saddle(ItemStack stack, @Nullable SoundCategory soundCategory) {
        this.setSaddled(true);
        if (soundCategory != null) {
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_HORSE_SADDLE, soundCategory, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void setJumpStrength(int strength) {
        if (this.canSprint()) {
            this.startSprint();
        }
    }

    @Override
    public void startJumping(int height) {
        if (this.canSprint()) {
            this.startSprint();
        }
    }

    @Override
    public void stopJumping() {
    }

    @Override
    public int getJumpCooldown() {
        return (this.isOstrichSprinting() || this.getCooldownTicks() > 0) ? 1 : 0;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity passenger = this.getFirstPassenger();
            if (passenger instanceof PlayerEntity player) {
                return player;
            }
        }
        return super.getControllingPassenger();
    }

    protected Vec2f getControlledRotation(LivingEntity controllingPassenger) {
        return new Vec2f(controllingPassenger.getPitch() * 0.5F, controllingPassenger.getYaw());
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 0.5F;
        float g = controllingPlayer.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }
        return new Vec3d(f, 0.0, g);
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);
        Vec2f vec2f = this.getControlledRotation(controllingPlayer);
        this.setRotation(vec2f.y, vec2f.x);
        this.prevYaw = this.bodyYaw = this.headYaw = this.getYaw();

        if (this.canSprint() && controllingPlayer instanceof ServerPlayerEntity serverPlayer && serverPlayer.getPlayerInput().jump()) {
            this.startSprint();
        }
    }

    @Override
    protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
        float baseSpeed = 0.28F;
        float maxHealth = this.getMaxHealth();
        float healthRatio = maxHealth > 0.0F ? (this.getHealth() / maxHealth) : 1.0F;
        healthRatio = MathHelper.clamp(healthRatio, 0.2F, 1.0F);
        float healthFactor = 0.5F + 0.5F * healthRatio;
        float speed = baseSpeed * healthFactor;
        if (this.isOstrichSprinting()) {
            speed *= 1.25F;
        }
        return speed;
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return new Vec3d(0.0, (dimensions.height() * 0.72F * scaleFactor), (-0.15F * scaleFactor))
                .rotateY(-this.getYaw() * (float)(Math.PI / 180.0));
    }

    @Override
    public boolean canWalkOnFluid(FluidState state) {
        if (this.isOstrichSprinting() && state.isIn(FluidTags.WATER)) {
            return !(state.getFluid() instanceof MudWaterFluid);
        }
        return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0F : 4.0F) * 0.3F;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.isOnGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
        }
        this.flapSpeed *= 0.9F;
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.85, 1.0));
        }
        this.flapProgress += this.flapSpeed * 2.0F;

        if (this.attackTicksLeft > 0) {
            this.attackTicksLeft--;
        }

        if (!this.getWorld().isClient()) {
            int sprint = this.getSprintTicks();
            if (sprint > 0) {
                sprint--;
                this.setSprintTicks(sprint);
                if (sprint == 0) {
                    this.setOstrichSprinting(false);
                    this.setCooldownTicks(400);
                }
            } else {
                int cooldown = this.getCooldownTicks();
                if (cooldown > 0) {
                    cooldown--;
                    this.setCooldownTicks(cooldown);
                }
            }

            EntityAttributeInstance speedAttr = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
            if (speedAttr != null && this.getMaxHealth() > 0.0F) {
                float healthRatio = MathHelper.clamp(this.getHealth() / this.getMaxHealth(), 0.2F, 1.0F);
                speedAttr.setBaseValue(0.24 * (0.5 + 0.5 * healthRatio));
            }
        }

        if (this.isOstrichSprinting()) {
            FluidState fluid = this.getWorld().getFluidState(this.getBlockPos());
            if (fluid.isIn(FluidTags.WATER) && !(fluid.getFluid() instanceof MudWaterFluid)) {
                Vec3d v = this.getVelocity();
                if (v.y < 0.0) {
                    this.setVelocity(v.x, 0.0, v.z);
                    this.setOnGround(true);
                }
            }

            if (!this.isOnGround()) {
                Vec3d vel = this.getVelocity();
                if (vel.y < 0.0) {
                    BlockPos belowFeet = BlockPos.ofFloored(this.getX(), this.getY() - 0.2, this.getZ());
                    if (this.getWorld().getBlockState(belowFeet).getCollisionShape(this.getWorld(), belowFeet).isEmpty()) {
                        Vec3d horiz = new Vec3d(vel.x, 0.0, vel.z);
                        if (horiz.lengthSquared() > 0.04) {
                            Vec3d dir = horiz.normalize();
                            BlockPos aheadGround = BlockPos.ofFloored(this.getX() + dir.x * 1.2, this.getY() - 0.2, this.getZ() + dir.z * 1.2);
                            BlockPos aheadSpace = aheadGround.up();
                            if (!this.getWorld().getBlockState(aheadGround).getCollisionShape(this.getWorld(), aheadGround).isEmpty()
                                    && this.getWorld().getBlockState(aheadSpace).getCollisionShape(this.getWorld(), aheadSpace).isEmpty()) {
                                this.setVelocity(vel.x, 0.0, vel.z);
                                this.setOnGround(true);
                            }
                        }
                    }
                }
            }
        }

        if (this.getWorld() instanceof ServerWorld serverWorld && this.isAlive() && !this.isBaby() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(serverWorld, ItemInit.OSTRICH_EGG);
            this.emitGameEvent(GameEvent.ENTITY_PLACE);

            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld)this.getWorld(), true);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // 1. Allow spawn egg to spawn baby ostrich when clicked on adult
        if (itemStack.getItem() instanceof SpawnEggItem) {
            return super.interactMob(player, hand);
        }

        // 2. Baby ostrich interactions: feeding to accelerate growth & heal
        if (this.isBaby()) {
            if (itemStack.isOf(Items.GLISTERING_MELON_SLICE) || itemStack.isOf(Items.MELON_SLICE)) {
                this.eat(player, hand, itemStack);
                this.growUp(toGrowUpAge(-this.getBreedingAge()), true);
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.0F, 1.25F);
                this.emitGameEvent(GameEvent.EAT);
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(itemStack.isOf(Items.GLISTERING_MELON_SLICE) ? 4.0F : 1.0F);
                }
                return ActionResult.SUCCESS;
            }
            return super.interactMob(player, hand);
        }

        // 3. Adult: Food interaction
        // 3a. Melon Slice: ONLY heals 1 HP if damaged, but eats even if at full health. Cannot tame, cannot breed.
        if (itemStack.isOf(Items.MELON_SLICE)) {
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(1.0F);
            }
            itemStack.decrementUnlessCreative(1, player);
            this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.0F, 1.0F);
            this.emitGameEvent(GameEvent.EAT);
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            }
            return ActionResult.SUCCESS;
        }

        // 3b. Glistering Melon Slice: Taming (if untamed) OR Breeding & Healing (if tamed)
        if (itemStack.isOf(Items.GLISTERING_MELON_SLICE)) {
            // Untamed: Attempts taming (+ heals 4 HP if injured)
            if (!this.isTamed()) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(4.0F);
                }
                itemStack.decrementUnlessCreative(1, player);
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.0F, 1.0F);
                this.emitGameEvent(GameEvent.EAT);
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    if (this.random.nextInt(3) == 0) {
                        this.setTamed(true);
                        serverWorld.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                    } else {
                        serverWorld.sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                    }
                }
                return ActionResult.SUCCESS;
            }

            // Tamed: Healing and/or Breeding (requires ostrich to be tamed first)
            boolean fed = false;

            // Healing if injured
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(4.0F);
                fed = true;
            }

            // Breeding if ready
            if (this.canEat()) {
                this.lovePlayer(player);
                fed = true;
            }

            if (fed) {
                itemStack.decrementUnlessCreative(1, player);
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.0F, 1.0F);
                this.emitGameEvent(GameEvent.EAT);
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        }

        // 4. Adult: Shears to remove saddle
        if (itemStack.isOf(Items.SHEARS) && this.isSaddled() && !this.hasPassengers()) {
            this.setSaddled(false);
            this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                this.dropItem(serverWorld, Items.SADDLE);
                itemStack.damage(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ActionResult.SUCCESS;
        }

        // 5. Adult: Saddle equipping
        if (itemStack.isOf(Items.SADDLE)) {
            ActionResult result = itemStack.useOnEntity(player, this, hand);
            if (result.isAccepted()) {
                return result;
            }
        }

        // 6. Adult: Mounting if saddled
        if (this.isSaddled() && !player.shouldCancelInteraction()) {
            if (!this.getWorld().isClient()) {
                player.startRiding(this);
            }
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return (this.isOstrichSprinting() || this.getVelocity().horizontalLengthSquared() > 2.5E-7F) && this.random.nextInt(3) == 0;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.angryAt;
    }

    @Override
    public boolean shouldAngerAt(LivingEntity entity, ServerWorld world) {
        if (this.isBaby()) {
            return false;
        }
        return Angerable.super.shouldAngerAt(entity, world);
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean bl = super.tryAttack(world, target);
        if (bl) {
            this.playSound(SoundEvents.ENTITY_AXOLOTL_ATTACK, 1.0F, 1.0F);
            world.sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
        }
        return bl;
    }

    public void playPeckAnimation() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            this.attackTicksLeft = 11;
            this.idlingAnimationState.stop();
            this.attackAnimationState.start(this.age);
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 1.0F, 1.0F);
    }

    @Override
    protected void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(world, damageSource, causedByPlayer);
        if (this.isSaddled()) {
            this.dropItem(world, Items.SADDLE);
        }
        if (this.random.nextFloat() < 0.6667F) {
            this.dropStack(world, new ItemStack(ItemInit.OSTRICH_FEATHER));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            this.updateAnimations();
        }
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
        if (this.getWorld().isClient()) {
            this.idlingAnimationState.stop();
            this.attackAnimationState.stop();
            this.getHitAnimationState.startIfNotRunning(this.age);
            this.idleAnimationCooldown = 40;
        }
    }

    private boolean isMoving() {
        return this.limbAnimator.isLimbMoving() || this.getVelocity().horizontalLengthSquared() > 0.001;
    }

    private void updateAnimations() {
        if (this.hurtTime > 0) {
            this.idlingAnimationState.stop();
            this.attackAnimationState.stop();
            this.getHitAnimationState.startIfNotRunning(this.age);
            this.idleAnimationDuration = 0;
            this.idleAnimationCooldown = 40;
        } else if (this.attackTicksLeft > 0) {
            this.idlingAnimationState.stop();
            this.getHitAnimationState.stop();
            this.idleAnimationDuration = 0;
        } else {
            this.getHitAnimationState.stop();
            this.attackAnimationState.stop();
            if (this.isMoving()) {
                this.idlingAnimationState.stop();
                this.idleAnimationDuration = 0;
            } else if (this.idleAnimationDuration > 0) {
                this.idleAnimationDuration--;
                if (this.idleAnimationDuration <= 0) {
                    this.idlingAnimationState.stop();
                    this.idleAnimationCooldown = this.random.nextInt(60) + 40;
                }
            } else if (this.idleAnimationCooldown <= 0) {
                this.idleAnimationId = this.random.nextInt(4);
                int durationTicks;
                if (this.isBaby()) {
                    durationTicks = switch (this.idleAnimationId) {
                        case 1 -> 30;
                        case 2 -> 45;
                        case 3 -> 35;
                        default -> 30;
                    };
                } else {
                    durationTicks = 30;
                }
                this.idleAnimationDuration = durationTicks;
                this.idlingAnimationState.start(this.age);
            } else {
                this.idleAnimationCooldown--;
            }
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.GLISTERING_MELON_SLICE);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        OstrichEntity child = EntityInit.OSTRICH.create(world, SpawnReason.BREEDING);
        if (child != null && this.isTamed()) {
            if (entity instanceof OstrichEntity otherParent && otherParent.isTamed()) {
                child.setTamed(true);
            }
        }
        return child;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        float maxHealth = 12.0F + this.random.nextInt(9);
        EntityAttributeInstance maxHealthAttr = this.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(maxHealth);
            this.setHealth(maxHealth);
        }
        return data;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        nbt.putBoolean("Tamed", this.isTamed());
        nbt.putBoolean("Saddled", this.isSaddled());
        nbt.putInt("SprintTicks", this.getSprintTicks());
        nbt.putInt("CooldownTicks", this.getCooldownTicks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.getWorld(), nbt);
        this.setTamed(nbt.getBoolean("Tamed"));
        this.setSaddled(nbt.getBoolean("Saddled"));
        this.setSprintTicks(nbt.getInt("SprintTicks"));
        this.setCooldownTicks(nbt.getInt("CooldownTicks"));
    }
}
