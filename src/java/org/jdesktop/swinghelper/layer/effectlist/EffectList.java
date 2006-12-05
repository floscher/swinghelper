package org.jdesktop.swinghelper.layer.effectlist;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.shaper.Shaper;
import org.jdesktop.swinghelper.layer.painter.Painter;

import java.awt.image.BufferedImageOp;

public interface EffectList {
    public BufferedImageOp getBufferedImageOp(BufferedImageOp bio, JXLayer l);
    public float getAlpha(float alpha, JXLayer l);
    public Painter getBackgroundPainter(Painter p, JXLayer l);
    public boolean isBackgroundPainterOrigin(boolean b, JXLayer l);
    public Painter getForegroundPainter(Painter p, JXLayer l);
    public boolean isForegroundPainterOrigin(boolean b, JXLayer l);
    public Shaper getShaper(Shaper s, JXLayer l);
}
