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

import org.jdesktop.swinghelper.layer.item.LayerItem;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.Map;

/**
 * State model for {@link org.jdesktop.swinghelper.layer.painter.Painter}
 * which can use data from its <code>PainterModel</code> during painting routine
 * and <code>MouseEvent</code>s filtering.
 *
 * @see org.jdesktop.swinghelper.layer.painter.Painter
 * @see org.jdesktop.swinghelper.layer.JXLayer
 * @see DefaultPainterModel
 */
public interface PainterModel extends LayerItem {
    /**
     * Gets the current clipping shape.
     *
     * @return  <code>Shape</code> object representing the
     *         current clipping area, or <code>null</code> if
     *         no clip is set.
     */
    public Shape getClip();

    /**
     * Sets the clipping shape
     * which can be <code>null</code>
     *
     * @param clip the <code>Shape</code> to use to set the clip
     */
    public void setClip(Shape clip);

    /**
     * Gets the current <code>Composite</code>  
     * 
     * @return the current <code>Composite</code>
     */
    public Composite getComposite();

    public void setComposite(Composite composite);

    public float getAlpha();

    public void setAlpha(float alpha);

    public Map<Key, Object> getRenderingHints();

    public void setRenderingHints(Map<Key, Object> renderingHints);

    public AffineTransform getTransform();

    public void setTransform(AffineTransform transform);
}
