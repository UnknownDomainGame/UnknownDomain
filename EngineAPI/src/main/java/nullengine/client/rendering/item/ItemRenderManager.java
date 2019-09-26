package nullengine.client.rendering.item;

import nullengine.exception.UninitializationException;
import nullengine.item.ItemStack;

import java.util.function.Supplier;

public interface ItemRenderManager {
    void render(ItemStack itemStack, float partial);

    static ItemRenderManager instance() {
        return ItemRenderManager.Internal.instance.get();
    }

    class Internal {
        private static Supplier<ItemRenderManager> instance = UninitializationException.supplier("ItemRenderManager is uninitialized");

        public static void setInstance(ItemRenderManager instance) {
            ItemRenderManager.Internal.instance = () -> instance;
        }
    }
}
