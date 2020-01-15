package nullengine.client.rendering3d.internal.impl.gl;

import nullengine.client.rendering3d.internal.ViewportHelper;
import nullengine.client.rendering3d.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ViewportHelperImpl implements ViewportHelper {

    private final List<Viewport> viewports = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void show(Viewport viewport) {
        viewports.add(viewport);
    }

    @Override
    public void hide(Viewport viewport) {
        viewports.remove(viewport);
    }

    public List<Viewport> getViewports() {
        return viewports;
    }
}
