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
 * State model for the {@link org.jdesktop.swinghelper.layer.painter.AbstractBufferedPainter}
 * which can use data from its model to set up
 * the {@link java.awt.Graphics2D} instance during painting routine
 */
public interface BufferedPainterModel extends PainterModel {

    /**
     * Sets whether or not this painter supports incremental updates.<br/>
     * If <code>incrementalUpdate</code> is <code>false</code>,
     * an {@link org.jdesktop.swinghelper.layer.painter.AbstractBufferedPainter}
     * which this model attached to, will skip all repaints with {@link java.awt.Graphics2D#getClip()}
     * is not equals to the {@link org.jdesktop.swinghelper.layer.JXLayer#getVisibleRect()}.
     * This is useful when you don't need incremental updates and want to speed the painting up
     * <p/>
     * <strong>Note:</strong>
     * If incremental updates are switched off and the layer's view has child components,
     * they might not be painted properly
     *
     * @param incrementalUpdate <code>true</code> if this model supports incremental updates,
     *                          <code>false</code> otherwise
     */
    public void setIncrementalUpdate(boolean incrementalUpdate);

    /**
     * Gets whether or not this painter supports incremental updates. 
     * 
     * @return <code>true</code> if this model supports incremental updates,
     *                          <code>false</code> otherwise
     */
    public boolean isIncrementalUpdate();
}