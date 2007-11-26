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

package org.jdesktop.swinghelper.layer.painter.model;

/**
 * The default implementation of the {@link BufferedPainterModel} interface,
 * which implements all methods and calls {@link DefaultBufferedPainterModel#fireLayerItemChanged()}
 * when any of its state is changed  
 * 
 * @see org.jdesktop.swinghelper.layer.painter.AbstractBufferedPainter
 */
public class DefaultBufferedPainterModel extends DefaultPainterModel implements BufferedPainterModel {
    private boolean isIncrementalUpdate;

    /**
     * {@inheritDoc} 
     */
    public boolean isIncrementalUpdate() {
        return isIncrementalUpdate;
    }

    /**
     * {@inheritDoc} 
     */
    public void setIncrementalUpdate(boolean isIncrementalUpdate) {
        this.isIncrementalUpdate = isIncrementalUpdate;
        fireLayerItemChanged();
    }
}