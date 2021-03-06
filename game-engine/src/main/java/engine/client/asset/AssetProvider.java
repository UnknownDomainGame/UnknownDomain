package engine.client.asset;

import engine.client.asset.exception.AssetLoadException;
import engine.client.asset.exception.AssetNotFoundException;

import javax.annotation.Nonnull;

public interface AssetProvider<T> {

    void init(AssetManager manager, AssetType<T> type);

    void register(Asset<T> asset);

    void unregister(Asset<T> asset);

    @Nonnull
    T loadDirect(AssetURL url) throws AssetLoadException, AssetNotFoundException;

    void dispose();
}
