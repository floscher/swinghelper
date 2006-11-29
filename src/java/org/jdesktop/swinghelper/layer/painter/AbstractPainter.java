package org.jdesktop.swinghelper.layer.painter;

public abstract class AbstractPainter implements Painter {
    private boolean isPaintingOrigin;

    protected AbstractPainter() {
    }

    protected AbstractPainter(boolean paintingOrigin) {
        isPaintingOrigin = paintingOrigin;
    }

    public boolean isPaintingOrigin() {
        return isPaintingOrigin;
    }

    public void setPaintingOrigin(boolean paintingOrigin) {
        isPaintingOrigin = paintingOrigin;
    }
}


