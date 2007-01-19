package org.jdesktop.swinghelper.layer.effect;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.ExtendedChangeSupport;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;

public abstract class Effect <V extends JComponent> {
    private boolean isEnabled;
    private ExtendedChangeSupport changeSupport;

    protected Effect() {
        changeSupport = new ExtendedChangeSupport(this);
        setEnabled(true);
    }

    public abstract BufferedImage apply(BufferedImage buf, JXLayer<V> l);
    
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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
