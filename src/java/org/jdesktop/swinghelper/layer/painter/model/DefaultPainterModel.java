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

/**
 * The default implementation of the {@link PainterModel} interface.<br/>
 * It implements all methods and calls {@link DefaultPainterModel#fireLayerItemChanged()}
 * when any of its state is changed  
 * 
 * @see org.jdesktop.swinghelper.layer.painter.AbstractPainter
 */
public class DefaultPainterModel
        extends AbstractLayerItem implements PainterModel {
    private Shape shape;
    private Composite composite;
    private Map<Key, Object> renderingHints;
    private AffineTransform transform;

    /**
     * Creates a new {@link DefaultPainterModel}
     */
    public DefaultPainterModel() {         
        renderingHints = new HashMap<Key, Object>();
    }

    /**
     * {@inheritDoc} 
     */
    public Shape getClip() {
        return shape;
    }

    /**
     * {@inheritDoc} 
     */
    public void setClip(Shape shape) {
        this.shape = shape;
        fireLayerItemChanged();
    }

    /**
     * {@inheritDoc} 
     */
    public Composite getComposite() {
        return composite;
    }

    /**
     * {@inheritDoc} 
     */
    public void setComposite(Composite composite) {
        this.composite = composite;
        fireLayerItemChanged();
    }

    /**
     * {@inheritDoc} 
     */
    public float getAlpha() {
        if (composite instanceof AlphaComposite) {
            AlphaComposite ac = (AlphaComposite) composite;
            return ac.getAlpha();
        }
        return 1;
    }

    /**
     * {@inheritDoc} 
     */
    public void setAlpha(float alpha) {
        setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /**
     * {@inheritDoc} 
     */
    public Map<Key, Object> getRenderingHints() {
        return new HashMap<Key, Object>(renderingHints);
    }

    /**
     * {@inheritDoc} 
     */
    public void setRenderingHints(Map<Key, Object> renderingHints) {
        this.renderingHints = renderingHints == null ?
                new HashMap<Key, Object>() : new HashMap<Key, Object>(renderingHints);
        fireLayerItemChanged();
    }

    /**
     * {@inheritDoc} 
     */
    public AffineTransform getTransform() {
        return transform == null ? null : new AffineTransform(transform);
    }

    /**
     * {@inheritDoc} 
     */
    public void setTransform(AffineTransform transform) {
        this.transform = transform == null ? null : new AffineTransform(transform);
        fireLayerItemChanged();
    }
}
