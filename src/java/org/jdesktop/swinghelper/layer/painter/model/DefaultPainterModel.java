package org.jdesktop.swinghelper.layer.painter.model;

import org.jdesktop.swinghelper.layer.ExtendedChangeSupport;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

public class DefaultPainterModel implements PainterModel {
    private Composite composite;
    private Map<Key, Object> renderingHints;
    private AffineTransform transform;
    private ExtendedChangeSupport changeSupport;

    public DefaultPainterModel() {         
        renderingHints = new HashMap<Key, Object>();
        changeSupport = new ExtendedChangeSupport(this);
    }

    // Composite
    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
        fireStateChanged();
    }

    public float getAlpha() {
        if (composite instanceof AlphaComposite) {
            AlphaComposite ac = (AlphaComposite) composite;
            return ac.getAlpha();
        }
        return 1;
    }

    public void setAlpha(float alpha) {
        setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    // RenderingHints
    public Map<Key, Object> getRenderingHints() {
        return new HashMap<Key, Object>(renderingHints);
    }

    public void setRenderingHints(Map<Key, Object> renderingHints) {
        this.renderingHints = renderingHints == null ?
                new HashMap<Key, Object>() : new HashMap<Key, Object>(renderingHints);
        fireStateChanged();
    }

    // AffineTransform
    public AffineTransform getTransform() {
        return transform == null ? null : new AffineTransform(transform);
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform == null ? null : new AffineTransform(transform);
        fireStateChanged();
    }

    // Notifications
    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public ChangeListener[] getChangeListeners() {
        return changeSupport.getChangeListeners();
    }

    protected void fireStateChanged() {
        changeSupport.fireStateChanged();
    }
}
