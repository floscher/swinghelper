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

/**
 * <p> A convenience interface to be implemented
 * by any <code>JXLayer</code> painting delegate
 *
 * @see AbstractLayerItem
 * @see Painter
 * @see PainterModel
 * @see Effect 
 */
public interface LayerItem {
    /**
     * Determines whether this component is enabled.
     * An enabled item takes part in painting process
     * when disabled ones are not taken into account
     *
     * @return <code>true</code> if the item is enabled,
     *         <code>false</code> otherwise
     */
    public boolean isEnabled();

    /**
     * Enables or disables this item,
     * depending on the value of the parameter <code>b</code>.
     * An enabled item takes part in painting process
     * when disabled ones are not taken into account
     *
     * @param b If <code>true</code>, this item is enabled;
     *          otherwise this item is disabled
     */
    public void setEnabled(boolean b);

    /**
     * Adds a <code>LayerItemListener</code> to the LayerItem
     *
     * @param l the listener to be added
     */
    public void addLayerItemListener(LayerItemListener l);

    /**
     * Removes a <code>LayerItemListener</code> from the LayerItem
     *
     * @param l the listener to be removed
     */
    public void removeLayerItemListener(LayerItemListener l);

    /**
     * Returnes an array of all the <code>LayerItemListener</code>s added
     * to this LayerItem with <code>addLayerItemListener</code>
     *
     * @return all of the <code>LayerItemListener</code>s added or
     *         an empty array if no listeners have been added
     */
    public LayerItemListener[] getLayerItemListeners();
}
