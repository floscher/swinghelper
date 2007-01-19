package org.jdesktop.swinghelper.layer.painter.model;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.Map;

public interface PainterModel {
    public void addChangeListener(ChangeListener l);
    public void removeChangeListener(ChangeListener l);

    public Composite getComposite();
    public void setComposite(Composite composite);
    public float getAlpha();
    public void setAlpha(float alpha);

    public Map<Key, Object> getRenderingHints();
    public void setRenderingHints(Map<Key, Object> renderingHints);

    public AffineTransform getTransform();
    public void setTransform(AffineTransform transform);
}
