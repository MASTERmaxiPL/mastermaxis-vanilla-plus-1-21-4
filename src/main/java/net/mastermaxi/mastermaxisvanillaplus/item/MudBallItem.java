package net.mastermaxi.mastermaxisvanillaplus.item;

import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.MudBallEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;

public class MudBallItem extends Item {
    public MudBallItem(Settings settings) {
        super(settings);
    }

    // Use item to throw a Mud Ball
    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // Only throw on the server side
        if (!world.isClient) {
            MudBallEntity mudBall = new MudBallEntity(
                    EntityInit.MUD_BALL,
                    world,
                    player,
                    itemStack.copy() // Pass the item stack to the entity
            );

            mudBall.setPosition(
                    player.getX() - (double)(MathHelper.cos(player.getYaw() / 180.0F * (float) Math.PI) * 0.1F), // Reduce the offset (e.g., 0.1F)
                    player.getEyeY() - 0.05D, // Place slightly below eye level, closer to the player
                    player.getZ() - (double)(MathHelper.sin(player.getYaw() / 180.0F * (float) Math.PI) * 0.1F)
            );
            mudBall.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 0.6F, 1.0F); // Speed and accuracy

            world.spawnEntity(mudBall);

            // Play throw sound
            world.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_SNOWBALL_THROW,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

        // Consume one mud ball on throw
        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return ActionResult.SUCCESS.withNewHandStack(itemStack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}