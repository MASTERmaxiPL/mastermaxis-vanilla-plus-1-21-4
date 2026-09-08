package net.mastermaxi.mastermaxisvanillaplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.mastermaxi.mastermaxisvanillaplus.block.Blocks;
import net.mastermaxi.mastermaxisvanillaplus.entity.EntityInit;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.OstrichEntityModel;
import net.mastermaxi.mastermaxisvanillaplus.entity.client.OstrichEntityRenderer;
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

        EntityRendererRegistry.register(EntityInit.MUD_BALL, FlyingItemEntityRenderer::new);
        System.out.println("Mud Ball renderer registered!");

        EntityRendererRegistry.register(EntityInit.OSTRICH_EGG, FlyingItemEntityRenderer::new);

        FluidRenderHandlerRegistry.INSTANCE.register(FluidInit.MUD_WATER_STILL, FluidInit.MUD_WATER_FLOWING, SimpleFluidRenderHandler.coloredWater(0x5C3A1E));
        System.out.println("Mud Water renderer registered!");

        EntityModelLayerRegistry.registerModelLayer(OstrichEntityModel.OSTRICH, OstrichEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(EntityInit.OSTRICH, OstrichEntityRenderer::new);
        System.out.println("Ostrich renderer registered!");

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), FluidInit.MUD_WATER_STILL, FluidInit.MUD_WATER_FLOWING);
        System.out.println("Mud Water BlockRenderLayerMap renderer registered!");

        net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.DEFAULT,
                Blocks.BAOBAB_LEAVES
        );
    }
}
