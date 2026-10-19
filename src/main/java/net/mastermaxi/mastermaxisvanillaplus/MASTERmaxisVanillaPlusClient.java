package net.mastermaxi.mastermaxisvanillaplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.BabyOstrichEntityModel;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.OstrichEntityModel;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.OstrichEntityRenderer;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.PigMuddedTopEntityModel;
import net.mastermaxi.mastermaxisvanillaplus.fluid.FluidInit;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.world.biome.FoliageColors;

public class MASTERmaxisVanillaPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Registering Renderers");
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MUDSTONE_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BAOBAB_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BAOBAB_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.REDTHORN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GRASSY_RED_SOIL, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.INFECTED_MOUND, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TERMITE_TRAIL, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BAOBAB_LEAVES, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BAOBAB_LEAVES_FLOWERS, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BAOBAB_LEAVES_FRUIT, RenderLayer.getCutoutMipped());

        net.minecraft.client.render.block.entity.BlockEntityRendererFactories.register(
                net.mastermaxi.mastermaxisvanillaplus.block.BlockEntities.INFECTED_WOOD,
                net.mastermaxi.mastermaxisvanillaplus.block.client.InfectedWoodBlockEntityRenderer::new
        );

        EntityRendererRegistry.register(EntityInit.MUD_BALL, FlyingItemEntityRenderer::new);
        System.out.println("Mud Ball renderer registered!");

        EntityRendererRegistry.register(EntityInit.OSTRICH_EGG, FlyingItemEntityRenderer::new);

        // Register renderers for new shooting entities
        EntityRendererRegistry.register(EntityInit.HEAVY_ARROW, net.minecraft.client.render.entity.ArrowEntityRenderer::new);
        EntityRendererRegistry.register(EntityInit.BOLT, net.minecraft.client.render.entity.ArrowEntityRenderer::new);

        FluidRenderHandlerRegistry.INSTANCE.register(FluidInit.MUD_WATER_STILL, FluidInit.MUD_WATER_FLOWING, SimpleFluidRenderHandler.coloredWater(0x5C3A1E));
        System.out.println("Mud Water renderer registered!");

        EntityModelLayerRegistry.registerModelLayer(OstrichEntityModel.OSTRICH, OstrichEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BabyOstrichEntityModel.BABY_OSTRICH, BabyOstrichEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(PigMuddedTopEntityModel.LAYER_LOCATION, PigMuddedTopEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(EntityInit.OSTRICH, OstrichEntityRenderer::new);
        System.out.println("Ostrich renderer registered!");

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), FluidInit.MUD_WATER_STILL, FluidInit.MUD_WATER_FLOWING);
        System.out.println("Mud Water BlockRenderLayerMap renderer registered!");

        net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.DEFAULT,
                Blocks.BAOBAB_LEAVES,
                Blocks.BAOBAB_LEAVES_FLOWERS,
                Blocks.BAOBAB_LEAVES_FRUIT
        );

        net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : net.minecraft.world.biome.GrassColors.getDefaultColor(),
                Blocks.GRASSY_RED_SOIL
        );

        // Redthorn: biome layer (tintindex 0) gets grass color; spike layer has no tintindex so stays red
        net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.DEFAULT,
                Blocks.REDTHORN
        );

        net.minecraft.client.gui.screen.ingame.HandledScreens.register(
                net.mastermaxi.mastermaxisvanillaplus.screen.ModScreenHandlerTypes.FLETCHING_TABLE,
                net.mastermaxi.mastermaxisvanillaplus.client.gui.screen.FletchingTableScreen::new
        );

        net.minecraft.client.gui.screen.ingame.HandledScreens.register(
                net.mastermaxi.mastermaxisvanillaplus.screen.ModScreenHandlerTypes.BACKPACK,
                net.mastermaxi.mastermaxisvanillaplus.client.gui.screen.BackpackScreen::new
        );

        net.minecraft.client.gui.screen.ingame.HandledScreens.register(
                net.mastermaxi.mastermaxisvanillaplus.screen.ModScreenHandlerTypes.QUIVER,
                net.mastermaxi.mastermaxisvanillaplus.client.gui.screen.QuiverScreen::new
        );

        // Register backpack and quiver model layers
        EntityModelLayerRegistry.registerModelLayer(net.mastermaxi.mastermaxisvanillaplus.model.Leather_Backpack.LAYER_LOCATION, net.mastermaxi.mastermaxisvanillaplus.model.Leather_Backpack::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(net.mastermaxi.mastermaxisvanillaplus.model.Iron_Netherite_Backpack.LAYER_LOCATION, net.mastermaxi.mastermaxisvanillaplus.model.Iron_Netherite_Backpack::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(net.mastermaxi.mastermaxisvanillaplus.model.Backpack_With_Quiver.LAYER_LOCATION, net.mastermaxi.mastermaxisvanillaplus.model.Backpack_With_Quiver::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(net.mastermaxi.mastermaxisvanillaplus.model.Backpack_With_Quiver_Arrows.LAYER_LOCATION, net.mastermaxi.mastermaxisvanillaplus.model.Backpack_With_Quiver_Arrows::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(net.mastermaxi.mastermaxisvanillaplus.model.Ender_Backpack.LAYER_LOCATION, net.mastermaxi.mastermaxisvanillaplus.model.Ender_Backpack::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(net.mastermaxi.mastermaxisvanillaplus.model.QuiverModel.LAYER_LOCATION, net.mastermaxi.mastermaxisvanillaplus.model.QuiverModel::getTexturedModelData);

        // Register backpack entity renderer
        EntityRendererRegistry.register(EntityInit.BACKPACK, net.mastermaxi.mastermaxisvanillaplus.entity.client.BackpackEntityRenderer::new);

        // Register Accessory renderers for worn backpacks and quiver
        net.mastermaxi.mastermaxisvanillaplus.client.renderer.BackpackAccessoryRenderer accessoryRenderer = new net.mastermaxi.mastermaxisvanillaplus.client.renderer.BackpackAccessoryRenderer();
        io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerRenderer(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.LEATHER_BACKPACK, () -> accessoryRenderer);
        io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerRenderer(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_BACKPACK, () -> accessoryRenderer);
        io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerRenderer(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.IRON_BACKPACK_WITH_QUIVER, () -> accessoryRenderer);
        io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerRenderer(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.NETHERITE_BACKPACK, () -> accessoryRenderer);
        io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerRenderer(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.ENDER_BACKPACK, () -> accessoryRenderer);
        io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerRenderer(net.mastermaxi.mastermaxisvanillaplus.item.ItemInit.QUIVER, () -> accessoryRenderer);

        // Register Keybindings
        KEY_OPEN_BACKPACK = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(
                new net.minecraft.client.option.KeyBinding(
                        "key.mastermaxisvanillaplus.open_backpack",
                        org.lwjgl.glfw.GLFW.GLFW_KEY_B,
                        "category.mastermaxisvanillaplus"
                )
        );
        KEY_PLACE_BACKPACK = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(
                new net.minecraft.client.option.KeyBinding(
                        "key.mastermaxisvanillaplus.place_backpack",
                        org.lwjgl.glfw.GLFW.GLFW_KEY_V,
                        "category.mastermaxisvanillaplus"
                )
        );

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KEY_OPEN_BACKPACK.wasPressed()) {
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.mastermaxi.mastermaxisvanillaplus.network.OpenBackpackC2SPayload());
            }
            while (KEY_PLACE_BACKPACK.wasPressed()) {
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.mastermaxi.mastermaxisvanillaplus.network.PlaceBackpackC2SPayload());
            }
        });
    }

    public static net.minecraft.client.option.KeyBinding KEY_OPEN_BACKPACK;
    public static net.minecraft.client.option.KeyBinding KEY_PLACE_BACKPACK;
}
