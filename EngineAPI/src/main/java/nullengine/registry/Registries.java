package nullengine.registry;

import nullengine.block.Block;
import nullengine.client.block.ClientBlock;
import nullengine.exception.NoInitializationException;
import nullengine.item.Item;
import nullengine.registry.game.BlockRegistry;
import nullengine.registry.game.ItemRegistry;

import java.lang.ref.WeakReference;

public final class Registries {

    private static WeakReference<BlockRegistry> blockRegistry;
    private static WeakReference<ItemRegistry> itemRegistry;
    private static WeakReference<Registry<ClientBlock>> clientBlockRegistry;

    public static BlockRegistry getBlockRegistry() {
        if (blockRegistry == null) {
            throw new NoInitializationException();
        }
        return blockRegistry.get();
    }

    public static ItemRegistry getItemRegistry() {
        if (itemRegistry == null) {
            throw new NoInitializationException();
        }
        return itemRegistry.get();
    }

    public static Registry<ClientBlock> getClientBlockRegistry() {
        if (clientBlockRegistry == null) {
            throw new NoInitializationException();
        }
        return clientBlockRegistry.get();
    }

    public static void init(RegistryManager registryManager) {
        blockRegistry = new WeakReference<>((BlockRegistry) registryManager.getRegistry(Block.class));
        itemRegistry = new WeakReference<>((ItemRegistry) registryManager.getRegistry(Item.class));
        clientBlockRegistry = new WeakReference<>(registryManager.getRegistry(ClientBlock.class));
    }

    private Registries() {
    }
}
