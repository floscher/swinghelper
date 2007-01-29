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

public interface PainterModel extends LayerItem {
    public Shape getClip();
    public void setClip(Shape clip);    
    
    public Composite getComposite();
    public void setComposite(Composite composite);
    public float getAlpha();
    public void setAlpha(float alpha);

    public Map<Key, Object> getRenderingHints();
    public void setRenderingHints(Map<Key, Object> renderingHints);

    public AffineTransform getTransform();
    public void setTransform(AffineTransform transform);
}
