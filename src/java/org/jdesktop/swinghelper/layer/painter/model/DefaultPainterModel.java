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

import org.jdesktop.swinghelper.layer.item.AbstractLayerItem;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

public class DefaultPainterModel 
        extends AbstractLayerItem implements PainterModel {
    private Shape shape;
    private Composite composite;
    private Map<Key, Object> renderingHints;
    private AffineTransform transform;

    public DefaultPainterModel() {         
        renderingHints = new HashMap<Key, Object>();
    }

    // Clip
    public Shape getClip() {
        return shape;
    }

    public void setClip(Shape shape) {
        this.shape = shape;
        fireLayerItemChanged();
    }

    // Composite
    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
        fireLayerItemChanged();
    }

    public float getAlpha() {
        if (composite instanceof AlphaComposite) {
            AlphaComposite ac = (AlphaComposite) composite;
            return ac.getAlpha();
        }
        return 1;
    }

    public void setAlpha(float alpha) {
        setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    // RenderingHints
    public Map<Key, Object> getRenderingHints() {
        return new HashMap<Key, Object>(renderingHints);
    }

    public void setRenderingHints(Map<Key, Object> renderingHints) {
        this.renderingHints = renderingHints == null ?
                new HashMap<Key, Object>() : new HashMap<Key, Object>(renderingHints);
        fireLayerItemChanged();
    }

    // AffineTransform
    public AffineTransform getTransform() {
        return transform == null ? null : new AffineTransform(transform);
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform == null ? null : new AffineTransform(transform);
        fireLayerItemChanged();
    }
}
