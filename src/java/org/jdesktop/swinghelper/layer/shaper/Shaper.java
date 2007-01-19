package org.jdesktop.swinghelper.layer.shaper;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.ExtendedChangeSupport;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public abstract class Shaper <V extends JComponent> {
    private boolean isEnabled;
    private ExtendedChangeSupport changeSupport;

    protected Shaper() {
        changeSupport = new ExtendedChangeSupport(this);
        isEnabled = true;
    }

    public abstract Shape getShape(JXLayer<V> l);

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
