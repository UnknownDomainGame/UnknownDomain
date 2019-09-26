package nullengine.client.rendering.entity;

import nullengine.client.rendering.RenderManager;
import nullengine.client.rendering.item.ItemRenderManager;
import nullengine.client.rendering.shader.ShaderManager;
import nullengine.entity.item.ItemEntity;
import org.joml.Matrix4f;

public class EntityItemRenderer implements EntityRenderer<ItemEntity> {

    private RenderManager context;

    @Override
    public void init(RenderManager context) {
        this.context = context;
    }

    @Override
    public boolean shouldRender(ItemEntity entity, double x, double y, double z, float partial) {
        return true;
    }

    @Override
    public void render(ItemEntity entity, double x, double y, double z, float partial) {
        ShaderManager.instance().setUniform("u_ModelMatrix", new Matrix4f()
                .translate((float) x, (float) y + .5f, (float) z)
                .scale(1f / 3, 1f / 3, 1f / 3)
                .rotateY(((int) entity.getWorld().getGameTick() % 360000) / 20f));

        ItemRenderManager.instance().render(entity.getItemStack(), partial);
    }

    @Override
    public void dispose() {

    }
}
