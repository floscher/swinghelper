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

import java.util.EventListener;

/**
 * <code>LayerItemListener</code>s are notified of changes
 * to a {@link LayerItem}
 * 
 * @see LayerItem
 */
public interface LayerItemListener extends EventListener {
    /**
     * Notification that <code>LayerItem</code> has changed.
     *
     * @param e a LayerItemEvent object
     */
    public void layerItemChanged(LayerItemEvent e);
}
