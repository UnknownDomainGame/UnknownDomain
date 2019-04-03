package unknowndomain.engine.item;

import unknowndomain.engine.block.RayTraceBlockHit;
import unknowndomain.engine.component.Component;
import unknowndomain.engine.entity.Entity;
import unknowndomain.engine.player.Player;
import unknowndomain.engine.world.World;

public interface ItemPrototype {
    UseBehavior DEFAULT_USE = (world, entity, item) -> {
    };
    UseBlockBehavior DEFAULT_USE_BLOCK = (player, item, hit) -> {
    };
    HitBlockBehavior DEFAULT_HIT_BLOCK = (world, player, item, hit) -> {
    };

    interface UseBehavior {
        void onUseStart(World world, Entity entity, Item item);

        default boolean onUsing(World world, Player player, Item item, int tickElapsed) {
            return false;
        }

        default void onUseStop(World world, Player player, Item item, int tickElapsed) {
        }
    }

    interface UseBlockBehavior extends Component {
        void onUseBlockStart(Player player, Item item, RayTraceBlockHit hit);

        default boolean onUsingBlock(Player player, Item item, RayTraceBlockHit hit, int tickElapsed) {
            return false;
        }

        default void onUseBlockStop(Player player, Item item, RayTraceBlockHit hit, int tickElapsed) {
        }
    }

    interface HitBlockBehavior {
        void onHit(World world, Player player, Item item, RayTraceBlockHit hit);

//        boolean onKeep(Player player, Item item, BlockPrototype.Hit hit, int tickElapsed);

//        void onUseStop(Player player, Item item, BlockPrototype.Hit hit, int tickElapsed);
    }

    interface HitEntityBehavior {
        void onStart(Player player, Item item, Entity entity);

        boolean onKeep(Player player, Item item, Entity entity, int tickElapsed);

        void onStart(Player player, Item item, Entity entity, int tickElapsed);
    }
}
