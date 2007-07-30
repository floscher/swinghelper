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
 * State model for {@link org.jdesktop.swinghelper.layer.painter.AbstractPainter}
 * which can use data from its model to set up 
 * the {@link Graphics2D} instance during painting routine
 *
 * @see org.jdesktop.swinghelper.layer.painter.Painter
 * @see org.jdesktop.swinghelper.layer.JXLayer
 * @see DefaultPainterModel
 * @see Graphics2D
 */
public interface PainterModel extends LayerItem {
    /**
     * Gets the current clipping shape.
     *
     * @return  {@link Shape} object representing the
     *         current clipping area, or <code>null</code> if
     *         no clip is set.
     */
    public Shape getClip();

    /**
     * Sets the clipping shape
     * which can be <code>null</code>
     *
     * @param clip the {@link Shape} to be used to set the clip
     */
    public void setClip(Shape clip);

    /**
     * Gets the current {@link Composite}  
     * 
     * @return the current {@link Composite}
     */
    public Composite getComposite();

    /**
     * Sets the {@link Composite}
     *  
     * @param composite the {@link Composite} to be used for rendering  
     */
    public void setComposite(Composite composite);

    /**
     * A convenience method which checks if <code>getComposite()</code> value is instance 
     * of {@link AlphaComposite} and returns its {@link java.awt.AlphaComposite#getAlpha()};
     * returns <code>1</code> otherwise
     *  
     * @return the alpha value of the current {@link Composite}
     * @see #getComposite()  
     * @see java.awt.AlphaComposite
     */
    public float getAlpha();

    /**
     * A convenience method which creates the <code>AlphaComposite</code> object 
     * that implements the opaque SRC_OVER rule with the specified alpha value
     * 
     * @param alpha the constant alpha to be multiplied with the alpha of
     * the source. <code>alpha</code> must be a floating point number in the
     * inclusive range [0.0,&nbsp;1.0].
     * @see #setComposite(java.awt.Composite) 
     * @see java.awt.AlphaComposite
     */
    public void setAlpha(float alpha);

    /**
     * Gets the preferences for the rendering algorithms
     *  
     * @return a reference to an instance of {@link RenderingHints}
     * that contains the current preferences.
     * @see RenderingHints
     */
    public Map<Key, Object> getRenderingHints();

    /**
     * Replaces the values of all preferences for the rendering
     * algorithms with the specified <code>hints</code>.
     *  
     * @param renderingHints the rendering hints to be set
     * @see RenderingHints
     */
    public void setRenderingHints(Map<Key, Object> renderingHints);

    /**
     *  Gets the current {@link AffineTransform}
     * 
     * @return the current {@link AffineTransform}
     * @see AffineTransform 
     */
    public AffineTransform getTransform();

    /**
     * Sets the {@link AffineTransform} for this model
     * 
     * @param transform the {@link AffineTransform} to be used for rendering
     * @see AffineTransform 
     */
    public void setTransform(AffineTransform transform);
}
