import java.util.ArrayList;

import biuoop.DrawSurface;

abstract class Renderable {
    private int z;

    public void setZIndex(int z) {
        this.z = z;
    }

    public int getZIndex() {
        return z;
    }

    abstract void drawOn(DrawSurface surface);

    static void drawRenderTargets(ArrayList<Renderable> renderTargets, DrawSurface surface) {
        renderTargets.sort(
            (a, b) -> Integer.compare(a.z, b.z) // sort by z-index
        );
        for (Renderable target: renderTargets) {
            target.drawOn(surface);
        }
    }
}
