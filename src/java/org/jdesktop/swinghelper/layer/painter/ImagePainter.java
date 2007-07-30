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
import org.jdesktop.swinghelper.layer.effect.Effect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The implementation of the {@link AbstractBufferedPainter}
 * which is designed for static images;
 * in contrast to another {@link AbstractBufferedPainter}'s subclasses,
 * the <code>ImagePainter</code> doesn't update its buffer during each painting,
 * the {@link #isBufferValid(Graphics2D, JXLayer)} always returns <code>true</code>
 * <p/>
 * The buffer is updated only when internal state of any of the child layer item
 * or painter itself is changed
 * 
 * @see #getImage()
 * @see #setImage(java.awt.Image) 
 * @see #validate() 
 */
public class ImagePainter<V extends JComponent>
        extends AbstractBufferedPainter<V> {
    private Image image;

    /**
     * Creates a new {@link ImagePainter}
     */
    protected ImagePainter() {
    }

    /**
     * Creates a new {@link ImagePainter}
     * with given <code>image</code>
     *  
     * @param image the image to be rendered by this painter 
     */
    public ImagePainter(Image image) {
        this(image, (Effect[]) null);
    }

    /**
     * Creates a new {@link ImagePainter}
     * with given <code>image</code> 
     * and collection of the {@link Effect}s
     * 
     * @param image the image to be rendered by this painter
     * @param effects the collection of the {@link Effect}s 
     * to be applied to the buffer of this painter
     */
    public ImagePainter(Image image, Effect... effects) {
        setImage(image);
        setEffects(effects);
    }

    /**
     * Gets the image to be rendered by this painter
     * 
     * @return the image to be rendered by this painter
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the image to be rendered by this painter
     * 
     * @param image the image to be rendered by this painter
     */
    public void setImage(Image image) {
        this.image = image;
        validate();
        fireLayerItemChanged();
    }

    /**
     * Creates a buffer for this painter
     * 
     * @param width the width of the returned <code>BufferedImage</code>
     * @param height the height of the returned <code>BufferedImage</code>
     * @return a <code>BufferedImage</code> to be used as a back buffer
     * for this painter
     */
    protected BufferedImage createBuffer(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * {@inheritDoc} 
     */
    protected boolean isPainterValid() {
        return getBuffer() != null;
    }

    /**
     * Since the buffer is not repainted during every painting for {@link ImagePainter},
     * this method always returns <code>true</code> 
     * 
     * @param g2 the {@link java.awt.Graphics2D} which will be used for painting
     * @param l the {@link JXLayer} to render for
     * @return <code>true</code> if painter's buffer is up-to-date
     * and shouldn't be repainted, if buffer is expired returns <code>false </code>
     */
    protected boolean isBufferValid(Graphics2D g2, JXLayer<V> l) {
        return true;
    }

    /**
     * {@inheritDoc} 
     */
    protected void validate() {
        Image image = getImage();
        if (image != null) {
            BufferedImage buffer = getBuffer();
            if (buffer == null ||
                    buffer.getWidth() != image.getWidth(null) ||
                    buffer.getHeight() != image.getHeight(null)) {
                buffer = createBuffer(image.getWidth(null), image.getHeight(null));
                setBuffer(buffer);
            }
            Graphics bufg = buffer.getGraphics();
            bufg.drawImage(image, 0, 0, null);
            bufg.dispose();
            applyEffects(null);
        } 
    }
}
