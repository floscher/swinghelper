package org.jdesktop.swinghelper.layer.effectlist;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.Painter;
import org.jdesktop.swinghelper.layer.shaper.Shaper;

import java.awt.image.BufferedImageOp;

public class DefaultEffectList implements EffectList {
    public float getAlpha(float alpha, JXLayer l) {
        return alpha;
    }

    public Painter getBackgroundPainter(Painter p, JXLayer l) {
        return p;
    }

    public BufferedImageOp getBufferedImageOp(BufferedImageOp bio, JXLayer l) {
        return bio;
    }

    public Painter getForegroundPainter(Painter p, JXLayer l) {
        return p;
    }

    public Shaper getShaper(Shaper s, JXLayer l) {
        return s;
    }

    public boolean isBackgroundPainterOrigin(boolean b, JXLayer l) {
        return b;
    }

    public boolean isForegroundPainterOrigin(boolean b, JXLayer l) {
        return b;
    }
}
