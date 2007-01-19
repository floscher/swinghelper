package org.jdesktop.swinghelper.layer;

import java.beans.PropertyChangeSupport;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public class ExtendedChangeSupport extends PropertyChangeSupport {
    private EventListenerList listenerList;
    private ChangeEvent changeEvent;
    private final Object source;

    public ExtendedChangeSupport(Object source) {
        super(source);
        this.source = source;
        this.listenerList = new EventListenerList();
    }

    public Object getSource() {
        return source;
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(ChangeListener.class);
    }

    public void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(getSource());
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }    
}
