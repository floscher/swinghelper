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

package org.jdesktop.swinghelper.layer.effect;

import org.jdesktop.swinghelper.layer.item.AbstractLayerItem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * This implementation of the <code>Effect</code> interface
 * delegates the processing of the <code>BufferedImage</code>
 * to the <code>BufferedImageOp</code> object.<br/>
 * If <code>BufferedImageOp</code> is set to <code>null</code>
 * for this <code>ImageOpEffect</code>, 
 * <code>apply</code> method takes no action 
 * 
 * @see BufferedImageOp
 * @see java.awt.image.AffineTransformOp
 * @see java.awt.image.BandCombineOp
 * @see java.awt.image.ColorConvertOp
 * @see java.awt.image.ConvolveOp
 * @see java.awt.image.LookupOp
 * @see java.awt.image.RescaleOp
 */
public class ImageOpEffect extends AbstractLayerItem implements Effect {
    private BufferedImageOp bufferedImageOp;
    private BufferedImage srcBuffer;

    /**
     * Creates an <code>ImageOpEffect</code> with <code>BufferedImageOp</code>
     * property set to <code>null</code> 
     */
    public ImageOpEffect() {
    }

    /**
     * Creates an <code>ImageOpEffect</code> with provided <code>bufferedImageOp</code>
     *  
     * @param bufferedImageOp the <code>BufferedImageOp</code> 
     */
    public ImageOpEffect(BufferedImageOp bufferedImageOp) {
        this.bufferedImageOp = bufferedImageOp;
    }

    /**
     * Returnes the <code>BufferedImageOp</code> which was set to this <code>ImageOpEffect</code>
     * or <code>null</code> if no <code>BufferedImageOp</code> was set
     *  
     * @return <code>BufferedImageOp</code> which was set to this <code>ImageOpEffect</code>
     */
    public BufferedImageOp getBufferedImageOp() {
        return bufferedImageOp;
    }

    /**
     * Sets the <code>BufferedImageOp</code> for this <code>ImageOpEffect</code>
     * which can be <code>null</code>  
     * 
     * @param bufferedImageOp <code>BufferedImageOp</code> for this <code>ImageOpEffect</code>
     */
    public void setBufferedImageOp(BufferedImageOp bufferedImageOp) {
        this.bufferedImageOp = bufferedImageOp;
        fireLayerItemChanged();
    }

    /**
     * {@inheritDoc}
     */
    public void apply(BufferedImage buffer, Shape clip) {
        if (bufferedImageOp == null) {
            return;
        }
        if (buffer == null) {
            throw new IllegalArgumentException("BufferedImage is null");
        }
        Rectangle bufferSize = new Rectangle(buffer.getWidth(), buffer.getHeight());

        if (clip == null) {
            clip = bufferSize;
        }
        Rectangle clipBounds = clip.getBounds().intersection(bufferSize);

        if (clipBounds.isEmpty() ||
                buffer.getWidth() <= clipBounds.x ||
                buffer.getHeight() <= clipBounds.y) {
            return;
        }

        int x = clipBounds.x;
        int y = clipBounds.y;
        int width = clipBounds.width;
        int height = clipBounds.height;

        if (buffer.getWidth() < x + width) {
            width = buffer.getWidth() - x;
        }
        if (buffer.getHeight() < y + height) {
            height = buffer.getHeight() - y;
        }

        if (srcBuffer == null ||
                srcBuffer.getWidth() != width ||
                srcBuffer.getHeight() != height) {
            srcBuffer = new BufferedImage(width, height, buffer.getType());
        }

        BufferedImage subImage
                = buffer.getSubimage(x, y, width, height);
        subImage.copyData(srcBuffer.getRaster());

        bufferedImageOp.filter(srcBuffer, subImage);
    }
}
