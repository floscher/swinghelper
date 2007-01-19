package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.ExtendedChangeSupport;
import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.model.DefaultPainterModel;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;
import org.jdesktop.swinghelper.layer.shaper.NullShaper;
import org.jdesktop.swinghelper.layer.shaper.Shaper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.Map;

public abstract class Painter<V extends JComponent> {
    private PainterModel model;
    private Shaper<V> clipShaper;
    private boolean isEnabled;
    private ExtendedChangeSupport changeSupport;
    private Handler handler;

    protected Painter() {
        isEnabled = true;
        changeSupport = new ExtendedChangeSupport(this);
        setModel(new DefaultPainterModel());
        setClipShaper(new NullShaper<V>());
    }

    public PainterModel getModel() {
        return model;
    }

    public void setModel(PainterModel model) {
        if (model == null) {
            throw new IllegalArgumentException(
                    "Null model is not supported; set DefaultPainterModel");
        }
        PainterModel oldModel = getModel();

        if (model != oldModel) {
            if (oldModel != null) {
                oldModel.removeChangeListener(getHandler());
            }
            model.addChangeListener(getHandler());
            this.model = model;
            fireStateChanged();
        }
    }

    public Shaper<V> getClipShaper() {
        return clipShaper;
    }

    public void setClipShaper(Shaper<V> clipShaper) {
        if (clipShaper == null) {
            throw new IllegalArgumentException(
                    "Null shaper is not supported; set DefaultShaper");
        }
        Shaper<V> oldShaper = getClipShaper();

        if (clipShaper != oldShaper) {
            if (oldShaper != null) {
                oldShaper.removeChangeListener(getHandler());
            }
            clipShaper.addChangeListener(getHandler());
        }
        this.clipShaper = clipShaper;
        fireStateChanged();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        fireStateChanged();
    }

    protected void configure(Graphics2D g2, JXLayer<V> l) {
        Shaper<V> clipShaper = getClipShaper();
        if (clipShaper.isEnabled()) {
            Shape shape = clipShaper.getShape(l);
            if (shape != null) {
                g2.setClip(shape);
            }            
        }
        // Applying model settings
        Map<Key, Object> hints = getModel().getRenderingHints();
        for (RenderingHints.Key key : hints.keySet()) {
            Object value = hints.get(key);
            if (value != null) {
                g2.setRenderingHint(key, hints.get(key));
            }
        }
        Composite composite = getModel().getComposite();
        if (composite != null) {
            g2.setComposite(composite);
        }
        AffineTransform transform = getModel().getTransform();
        if (transform != null) {
            g2.setTransform(transform);
        }
    }

    public abstract void paint(Graphics2D g2, JXLayer<V> l);

    public void repaint() {
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

    protected Handler getHandler() {
        if (handler == null) {
            handler = createHandler();
        }
        return handler;
    }

    protected Handler createHandler() {
        return new Handler();
    }
    
    protected class Handler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }
}
