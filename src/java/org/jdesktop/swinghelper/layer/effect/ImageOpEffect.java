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

public class ImageOpEffect extends AbstractLayerItem implements Effect {
    private BufferedImageOp bufferedImageOp;
    private BufferedImage srcBuffer;

    public ImageOpEffect() {
    }

    public ImageOpEffect(BufferedImageOp bufferedImageOp) {
        this.bufferedImageOp = bufferedImageOp;
    }

    public BufferedImageOp getBufferedImageOp() {
        return bufferedImageOp;
    }

    public void setBufferedImageOp(BufferedImageOp bufferedImageOp) {
        this.bufferedImageOp = bufferedImageOp;
        repaint();
    }

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
