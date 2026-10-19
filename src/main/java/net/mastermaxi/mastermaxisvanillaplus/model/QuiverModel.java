package net.mastermaxi.mastermaxisvanillaplus.model;

import net.mastermaxi.mastermaxisvanillaplus.MASTERmaxisVanillaPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

public class QuiverModel extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(MASTERmaxisVanillaPlus.id("quiver"), "main");
    private final ModelPart quiver;
    private final ModelPart arrows;

    public QuiverModel(@SuppressWarnings("unused") ModelPart root) {
        super(createCustomModelPart(), RenderLayer::getEntityCutoutNoCull);
        this.quiver = this.root.getChild("quiver");
        this.arrows = this.quiver.getChild("arrows");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        return TexturedModelData.of(modelData, 16, 16);
    }

    public void setArrowsVisible(boolean visible) {
        this.arrows.visible = visible;
    }

    public ModelPart getQuiver() {
        return this.quiver;
    }

    private static ModelPart createCustomModelPart() {
        List<ModelPart.Quad> quiverQuads = new ArrayList<>();
        // Quiver box: x in [-1.5, 1.5], y in [-5.0, 5.0], z in [2.0, 5.0]
        // North face (z=2.0, uv=[0, 0, 3, 10])
        quiverQuads.add(makeQuad(1.5F, -5.0F, 2.0F, -1.5F, -5.0F, 2.0F, -1.5F, 5.0F, 2.0F, 1.5F, 5.0F, 2.0F, 0.0F, 0.0F, 3.0F, 10.0F, Direction.NORTH));
        // East face (x=1.5, uv=[3, 0, 6, 10])
        quiverQuads.add(makeQuad(1.5F, -5.0F, 5.0F, 1.5F, -5.0F, 2.0F, 1.5F, 5.0F, 2.0F, 1.5F, 5.0F, 5.0F, 3.0F, 0.0F, 6.0F, 10.0F, Direction.EAST));
        // South face (z=5.0, uv=[6, 0, 9, 10])
        quiverQuads.add(makeQuad(-1.5F, -5.0F, 5.0F, 1.5F, -5.0F, 5.0F, 1.5F, 5.0F, 5.0F, -1.5F, 5.0F, 5.0F, 6.0F, 0.0F, 9.0F, 10.0F, Direction.SOUTH));
        // West face (x=-1.5, uv=[9, 0, 12, 10])
        quiverQuads.add(makeQuad(-1.5F, -5.0F, 2.0F, -1.5F, -5.0F, 5.0F, -1.5F, 5.0F, 5.0F, -1.5F, 5.0F, 2.0F, 9.0F, 0.0F, 12.0F, 10.0F, Direction.WEST));
        // Up face (y=-5.0, uv=[0, 10, 3, 13])
        quiverQuads.add(makeQuad(1.5F, -5.0F, 2.0F, -1.5F, -5.0F, 2.0F, -1.5F, -5.0F, 5.0F, 1.5F, -5.0F, 5.0F, 0.0F, 10.0F, 3.0F, 13.0F, Direction.UP));
        // Down face (y=5.0, uv=[3, 10, 6, 13])
        quiverQuads.add(makeQuad(1.5F, 5.0F, 5.0F, -1.5F, 5.0F, 5.0F, -1.5F, 5.0F, 2.0F, 1.5F, 5.0F, 2.0F, 3.0F, 10.0F, 6.0F, 13.0F, Direction.DOWN));

        ModelPart.Cuboid quiverCuboid = new CustomCuboid(quiverQuads.toArray(new ModelPart.Quad[0]));

        List<ModelPart.Quad> arrowQuads = new ArrayList<>();
        // Arrow element 1
        arrowQuads.add(makeQuad(1.5F, -6.0F, 5.0F, -1.5F, -6.0F, 5.0F, -1.5F, -5.0F, 5.0F, 1.5F, -5.0F, 5.0F, 6.0F, 10.0F, 9.0F, 11.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-1.5F, -6.0F, 5.0F, 1.5F, -6.0F, 5.0F, 1.5F, -5.0F, 5.0F, -1.5F, -5.0F, 5.0F, 9.0F, 10.0F, 12.0F, 11.0F, Direction.SOUTH));
        // Arrow element 2
        arrowQuads.add(makeQuad(0.5F, -7.0F, 5.0F, -0.5F, -7.0F, 5.0F, -0.5F, -6.0F, 5.0F, 0.5F, -6.0F, 5.0F, 10.0F, 12.0F, 11.0F, 13.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-0.5F, -7.0F, 5.0F, 0.5F, -7.0F, 5.0F, 0.5F, -6.0F, 5.0F, -0.5F, -6.0F, 5.0F, 12.0F, 10.0F, 13.0F, 11.0F, Direction.SOUTH));
        // Arrow element 3
        arrowQuads.add(makeQuad(0.5F, -6.0F, 4.0F, -1.5F, -6.0F, 4.0F, -1.5F, -5.0F, 4.0F, 0.5F, -5.0F, 4.0F, 12.0F, 4.0F, 14.0F, 5.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-1.5F, -6.0F, 4.0F, 0.5F, -6.0F, 4.0F, 0.5F, -5.0F, 4.0F, -1.5F, -5.0F, 4.0F, 12.0F, 5.0F, 14.0F, 6.0F, Direction.SOUTH));
        // Arrow element 4
        arrowQuads.add(makeQuad(-0.5F, -7.0F, 4.0F, -1.5F, -7.0F, 4.0F, -1.5F, -6.0F, 4.0F, -0.5F, -6.0F, 4.0F, 11.0F, 12.0F, 12.0F, 13.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-1.5F, -7.0F, 4.0F, -0.5F, -7.0F, 4.0F, -0.5F, -6.0F, 4.0F, -1.5F, -6.0F, 4.0F, 12.0F, 11.0F, 13.0F, 12.0F, Direction.SOUTH));
        // Arrow element 5
        arrowQuads.add(makeQuad(1.5F, -6.0F, 3.0F, -0.5F, -6.0F, 3.0F, -0.5F, -5.0F, 3.0F, 1.5F, -5.0F, 3.0F, 6.0F, 12.0F, 8.0F, 13.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-0.5F, -6.0F, 3.0F, 1.5F, -6.0F, 3.0F, 1.5F, -5.0F, 3.0F, -0.5F, -5.0F, 3.0F, 12.0F, 6.0F, 14.0F, 7.0F, Direction.SOUTH));
        // Arrow element 6
        arrowQuads.add(makeQuad(0.5F, -7.0F, 3.0F, -0.5F, -7.0F, 3.0F, -0.5F, -6.0F, 3.0F, 0.5F, -6.0F, 3.0F, 12.0F, 12.0F, 13.0F, 13.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-0.5F, -7.0F, 3.0F, 0.5F, -7.0F, 3.0F, 0.5F, -6.0F, 3.0F, -0.5F, -6.0F, 3.0F, 0.0F, 13.0F, 1.0F, 14.0F, Direction.SOUTH));
        // Arrow element 7
        arrowQuads.add(makeQuad(1.5F, -6.0F, 2.0F, -1.5F, -6.0F, 2.0F, -1.5F, -5.0F, 2.0F, 1.5F, -5.0F, 2.0F, 6.0F, 11.0F, 9.0F, 12.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-1.5F, -6.0F, 2.0F, 1.5F, -6.0F, 2.0F, 1.5F, -5.0F, 2.0F, -1.5F, -5.0F, 2.0F, 9.0F, 11.0F, 12.0F, 12.0F, Direction.SOUTH));
        // Arrow element 8
        arrowQuads.add(makeQuad(1.5F, -7.0F, 2.0F, -0.5F, -7.0F, 2.0F, -0.5F, -6.0F, 2.0F, 1.5F, -6.0F, 2.0F, 12.0F, 7.0F, 14.0F, 8.0F, Direction.NORTH));
        arrowQuads.add(makeQuad(-0.5F, -7.0F, 2.0F, 1.5F, -7.0F, 2.0F, 1.5F, -6.0F, 2.0F, -0.5F, -6.0F, 2.0F, 8.0F, 12.0F, 10.0F, 13.0F, Direction.SOUTH));
        // Arrow element 9 (rotated 90)
        arrowQuads.add(makeQuad(0.5F, -6.0F, 2.0F, 0.5F, -6.0F, 5.0F, 0.5F, -5.0F, 5.0F, 0.5F, -5.0F, 2.0F, 12.0F, 0.0F, 15.0F, 1.0F, Direction.WEST));
        arrowQuads.add(makeQuad(0.5F, -6.0F, 5.0F, 0.5F, -6.0F, 2.0F, 0.5F, -5.0F, 2.0F, 0.5F, -5.0F, 5.0F, 12.0F, 1.0F, 15.0F, 2.0F, Direction.EAST));
        // Arrow element 10 (rotated 90)
        arrowQuads.add(makeQuad(0.5F, -7.0F, 3.0F, 0.5F, -7.0F, 4.0F, 0.5F, -6.0F, 4.0F, 0.5F, -6.0F, 3.0F, 12.0F, 8.0F, 13.0F, 9.0F, Direction.WEST));
        arrowQuads.add(makeQuad(0.5F, -7.0F, 4.0F, 0.5F, -7.0F, 3.0F, 0.5F, -6.0F, 3.0F, 0.5F, -6.0F, 4.0F, 12.0F, 9.0F, 13.0F, 10.0F, Direction.EAST));
        // Arrow element 11 (rotated 90)
        arrowQuads.add(makeQuad(-0.5F, -6.0F, 2.0F, -0.5F, -6.0F, 5.0F, -0.5F, -5.0F, 5.0F, -0.5F, -5.0F, 2.0F, 12.0F, 2.0F, 15.0F, 3.0F, Direction.WEST));
        arrowQuads.add(makeQuad(-0.5F, -6.0F, 5.0F, -0.5F, -6.0F, 2.0F, -0.5F, -5.0F, 2.0F, -0.5F, -5.0F, 5.0F, 12.0F, 3.0F, 15.0F, 4.0F, Direction.EAST));
        // Arrow element 12 (rotated 90)
        arrowQuads.add(makeQuad(-0.5F, -7.0F, 3.0F, -0.5F, -7.0F, 4.0F, -0.5F, -6.0F, 4.0F, -0.5F, -6.0F, 3.0F, 1.0F, 13.0F, 2.0F, 14.0F, Direction.WEST));
        arrowQuads.add(makeQuad(-0.5F, -7.0F, 4.0F, -0.5F, -7.0F, 3.0F, -0.5F, -6.0F, 3.0F, -0.5F, -6.0F, 4.0F, 2.0F, 13.0F, 3.0F, 14.0F, Direction.EAST));

        ModelPart.Cuboid arrowCuboid = new CustomCuboid(arrowQuads.toArray(new ModelPart.Quad[0]));

        Map<String, ModelPart> quiverChildren = new HashMap<>();
        quiverChildren.put("arrows", new ModelPart(List.of(arrowCuboid), Map.of()));

        ModelPart quiverPart = new ModelPart(List.of(quiverCuboid), quiverChildren);
        quiverPart.setTransform(ModelTransform.of(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));

        Map<String, ModelPart> rootChildren = new HashMap<>();
        rootChildren.put("quiver", quiverPart);

        return new ModelPart(List.of(), rootChildren);
    }

    private static ModelPart.Quad makeQuad(
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float u1, float v1, float u2, float v2,
            Direction direction
    ) {
        return new ModelPart.Quad(
                new ModelPart.Vertex[]{
                        new ModelPart.Vertex(x0, y0, z0, 0.0F, 0.0F),
                        new ModelPart.Vertex(x1, y1, z1, 0.0F, 0.0F),
                        new ModelPart.Vertex(x2, y2, z2, 0.0F, 0.0F),
                        new ModelPart.Vertex(x3, y3, z3, 0.0F, 0.0F)
                },
                u1, v1, u2, v2,
                16.0F, 16.0F,
                false,
                direction
        );
    }

    private static class CustomCuboid extends ModelPart.Cuboid {
        private final ModelPart.Quad[] customSides;

        public CustomCuboid(ModelPart.Quad[] sides) {
            super(0, 0, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, false, 16.0F, 16.0F, Set.of());
            this.customSides = sides;
        }

        @Override
        public void renderCuboid(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, int color) {
            Matrix4f matrix4f = entry.getPositionMatrix();
            Vector3f vector3f = new Vector3f();

            for (ModelPart.Quad quad : this.customSides) {
                Vector3f vector3f2 = entry.transformNormal(quad.direction(), vector3f);
                float f = vector3f2.x();
                float g = vector3f2.y();
                float h = vector3f2.z();

                for (ModelPart.Vertex vertex : quad.vertices()) {
                    float i = vertex.pos().x() / 16.0F;
                    float j = vertex.pos().y() / 16.0F;
                    float k = vertex.pos().z() / 16.0F;
                    Vector3f vector3f3 = matrix4f.transformPosition(i, j, k, vector3f);
                    vertexConsumer.vertex(vector3f3.x(), vector3f3.y(), vector3f3.z(), color, vertex.u(), vertex.v(), overlay, light, f, g, h);
                }
            }
        }
    }
}
