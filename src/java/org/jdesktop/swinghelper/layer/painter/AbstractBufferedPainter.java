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

package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.item.LayerItemEvent;
import org.jdesktop.swinghelper.layer.effect.*;
import org.jdesktop.swinghelper.layer.painter.model.BufferedPainterModel;
import org.jdesktop.swinghelper.layer.painter.model.DefaultBufferedPainterModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * The default implementation of the buffered painter 
 * which paints itself to the back buffer first 
 * and then draws it to the {@link Graphics2D}
 * <p>
 * The main feature provided by {@link AbstractBufferedPainter} 
 * is ability to apply various {@link Effect}s to its content,
 * the most popular effect is {@link ImageOpEffect} 
 * which uses {@link BufferedImageOp} to filter the buffer
 * <p>
 * Note: to paint on the buffer override 
 * {@link #paintToBuffer(Graphics2D, JXLayer)} method
 * 
 * @see BufferedPainter
 * @see BufferedPainterModel
 * @see Effect
 * @see ImageOpEffect 
 */
abstract public class AbstractBufferedPainter<V extends JComponent>
        extends AbstractPainter<V> {

    private BufferedImage buffer;
    private Effect[] effects = new Effect[0];

    /**
     * Creates a new {@link AbstractBufferedPainter}
     * with a new {@link AbstractBufferedPainter} as its model
     */
    protected AbstractBufferedPainter() {
        this(new DefaultBufferedPainterModel());
    }

    /**
     * Creates a new {@link AbstractBufferedPainter}
     * with a specified painter model
     *
     * @param model {@link BufferedPainterModel} to be used for this painter
     */
    protected AbstractBufferedPainter(BufferedPainterModel model) {
        super(model);
    }

    /**
     * Gets the {@link BufferedPainterModel} of this painter
     * <p>
     * Note: this method never returns <code>null</code>
     *
     * @return the {@link BufferedPainterModel} of this painter
     */
    public BufferedPainterModel getModel() {
        return (BufferedPainterModel) super.getModel();
    }

    /**
     * Gets whether or not this painter supports incremental updates;
     * the default implementation reads it from the painter's model
     * <p>
     * If this method returns <code>false</code>,
     * this painter will skip all repaints with {@link java.awt.Graphics2D#getClip()}
     * is not equals to the {@link org.jdesktop.swinghelper.layer.JXLayer#getVisibleRect()}.
     * This is useful when you don't need incremental updates and want to speed the painting up,
     * moreover there are some {@link BufferedImageOp}s 
     * which don't work correctly with incremental updates
     * (the {@link ConvolveOp} is the most notable one) 
     *  
     * @param l the {@link JXLayer} to paint for
     * @return <code>true</code> if this model supports incremental updates,
     *                          <code>false</code> otherwise
     *
     * @see BufferedPainterModel#isIncrementalUpdate() 
     * @see BufferedPainterModel#setIncrementalUpdate(boolean) 
     * @see #getModel() 
     */
    protected boolean isIncrementalUpdate(JXLayer<V> l) {
        return getModel().isIncrementalUpdate();
    }

    /**
     * Gets the current buffer of this painter
     *
     *  @return the current buffer of this painter
     */
    public BufferedImage getBuffer() {
        return buffer;
    }

    /**
     * Sets the buffer for this painter
     * 
     * @param buffer the new buffer for this painter
     */
    protected void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

    /**
     * Sets the collection of the {@link Effect}s 
     * to be applied to the buffer of this painter
     *  
     * @param effects the collection of the {@link Effect}s 
     * to be applied to the buffer of this painter
     * 
     * @see Effect
     * @see ImageOpEffect
     */
    public void setEffects(Effect... effects) {
        if (effects == null) {
            effects = new Effect[0];
        }
        for (Effect effect : getEffects()) {
            effect.removeLayerItemListener(this);
        }
        this.effects = new Effect[effects.length];
        System.arraycopy(effects, 0, this.effects, 0, effects.length);
        for (Effect effect : effects) {
            effect.addLayerItemListener(this);
        }
        validate();
        fireLayerItemChanged();
    }

    /**
     * Gets the collection of the {@link Effect}s 
     * to be applied to the buffer of this painter
     *  
     * @return the collection of the {@link Effect}s 
     * to be applied to the buffer of this painter
     */
    public Effect[] getEffects() {
        Effect[] result = new Effect[effects.length];
        System.arraycopy(effects, 0, result, 0, result.length);
        return result;
    }

    /**
     * Iterates through all {@link Effect}s 
     * which were set for this painter 
     * and applies each one to the buffer
     *  
     * @param clip the current clipping shape
     * 
     * @see Effect
     * @see #getEffects()
     */
    protected void applyEffects(Shape clip) {
        if (getBuffer() == null) {
            throw new IllegalStateException("Buffer is null");
        }
        for (Effect e : getEffects()) {
            if (e.isEnabled()) {
                e.apply(getBuffer(), clip);
            }
        }
    }

    /**
     * The implementation of the {@link Painter#paint(Graphics2D, JXLayer)}
     * which renders the visual appearance of the given <code>JXLayer</code>
     * to the back buffer, applies all {@link Effect}s 
     * and then draws it to the given <code>Graphics2D</code>
     * <p>
     * Note: to paint on the back buffer, override {@link #paintToBuffer(Graphics2D, JXLayer)}
     *  
     * @param g2 the {@link java.awt.Graphics2D} to render to
     * @param l the {@link JXLayer} to render for
     */
    public void paint(Graphics2D g2, JXLayer<V> l) {
        if (isLayerValid(l) && isPainterValid()) {
            // if buffer is not valid it should be repainted
            if (!isBufferValid(g2, l)) {
                // if buffer format is not valid it should be recreated
                if (!isBufferFormatValid(g2, l)) {
                    setBuffer(createBuffer(g2, l.getWidth(), l.getHeight()));
                }
                Graphics2D bufg = (Graphics2D) getBuffer().getGraphics();
                if (isIncrementalUpdate(l)) {
                    bufg.setClip(g2.getClip());
                }
                paintToBuffer(bufg, l);
                applyEffects(bufg.getClip());
                bufg.dispose();
            }
            super.paint(g2, l);
            g2.drawImage(getBuffer(), 0, 0, null);
        }
    }

    /**
     * Renders the visual appearance of the given <code>JXLayer</code>
     * to the <code>Graphics2D</code> instance from the painter's back buffer;
     * Override this method for custom painting
     * 
     * @param g2 the {@link java.awt.Graphics2D} to render to
     * @param l the {@link JXLayer} to render for
     */
    protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
    }

    /**
     * Returns <code>true</code> if given {@link JXLayer}
     * is in valid state and painting should be performed;
     * otherwise returns <code>false</code>
     * 
     * @param l the {@link JXLayer} to be checked
     * @return <code>true</code> if given {@link JXLayer}
     * is in valid state and painting should be performed;
     * otherwise returns <code>false</code>
     */
    protected boolean isLayerValid(JXLayer<V> l) {
        return l.getWidth() != 0 && l.getHeight() != 0;
    }

    /**
     * Returns <code>true</code> if painter is in a valid state
     * and the painting should be performed, otherwise returns <code>false </code>
     * 
     * @return <code>true</code> if painter is in a valid state
     * and the painting should be performed, otherwise returns <code>false </code>
     */
    protected boolean isPainterValid() {
        return true;
    }

    /**
     * Returns <code>true</code> if painter's buffer is up-to-date
     * and shouldn't be repainted, if buffer is expired returns <code>false </code>
     *  
     * @param g2 the {@link java.awt.Graphics2D} which will be used for painting
     * @param l the {@link JXLayer} to render for
     * @return <code>true</code> if painter's buffer is up-to-date
     * and shouldn't be repainted, if buffer is expired returns <code>false </code>
     */
    protected boolean isBufferValid(Graphics2D g2, JXLayer<V> l) {
        return !isIncrementalUpdate(l) &&
                !l.getVisibleRect().equals(g2.getClipBounds());
    }

    /**
     * Returns <code>true</code> if buffer format is valid,
     * if it is invalid and should be recreated, returns <code>false</code>
     *  
     * @param g2 the {@link java.awt.Graphics2D} which will be used for painting 
     * @param l the {@link JXLayer} to render for
     * @return <code>true</code> if buffer format is valid,
     * if it is invalid and should be recreated, returns <code></code>
     */
    protected boolean isBufferFormatValid(Graphics2D g2, JXLayer<V> l) {
        BufferedImage buffer = getBuffer();
        return buffer != null &&
                buffer.getWidth() == l.getWidth() &&
                buffer.getHeight() == l.getHeight();
    }

    /**
     * Creates a buffer for this painter
     * 
     * @param g2 the {@link java.awt.Graphics2D} which will be used for painting
     * @param width the width of the returned <code>BufferedImage</code> 
     * @param height the height of the returned <code>BufferedImage</code>
     * @return a <code>BufferedImage</code> to be used as a back buffer
     * for this painter
     */
    protected BufferedImage createBuffer(Graphics2D g2, int width, int height) {
        return g2.getDeviceConfiguration().
                createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    /**
     * {@inheritDoc} 
     */
    public void layerItemChanged(LayerItemEvent e) {
        validate();
        super.layerItemChanged(e);
    }

    /**
     * This method is called each time when this painter
     * or ony of its child items are changed;
     * override this method if any additional actions
     * should be taken when painter is updated
     * 
     * @see ImagePainter 
     */
    protected void validate() {
    }
}
