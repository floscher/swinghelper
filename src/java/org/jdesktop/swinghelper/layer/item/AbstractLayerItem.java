/*
 * Copyright (C) 2006,2007 Alexander Potochkin
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swinghelper.layer.item;

import javax.swing.event.EventListenerList;
import java.awt.*;

abstract public class AbstractLayerItem implements LayerItem {
    private EventListenerList listenerList;
    private boolean isEnabled;

    protected AbstractLayerItem() {
        listenerList = new EventListenerList();
        setEnabled(true);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        fireLayerItemChanged();
    }

    public void addLayerItemListener(LayerItemListener l) {
        listenerList.add(LayerItemListener.class, l);
    }

    public void removeLayerItemListener(LayerItemListener l) {
        listenerList.remove(LayerItemListener.class, l);
    }

    public LayerItemListener[] getLayerItemListeners() {
        return (LayerItemListener[]) listenerList.getListeners(LayerItemListener.class);
    }

    protected void fireLayerItemChanged() {
        fireLayerItemChanged((Shape) null);
    }

    protected void fireLayerItemChanged(Shape clip) {
        fireLayerItemChanged(new LayerItemEvent(this, clip));
    }    

    private void fireLayerItemChanged(LayerItemEvent event) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == LayerItemListener.class) {
                ((LayerItemListener) listeners[i + 1]).layerItemChanged(event);
            }
        }
    }
}
