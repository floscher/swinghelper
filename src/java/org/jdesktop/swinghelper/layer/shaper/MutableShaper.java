package org.jdesktop.swinghelper.layer.shaper;

import org.jdesktop.swinghelper.layer.JXLayer;

import java.awt.*;

public class MutableShaper extends Shaper {
    private Shape shape;

    public MutableShaper() {
    }

    public MutableShaper(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        fireStateChanged();
    }

    public Shape getShape(JXLayer l) {
        return shape;
    }
}
