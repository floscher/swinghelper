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

public class ImagePainter<V extends JComponent>
        extends AbstractBufferedPainter<V> {
    private Image image;

    protected ImagePainter() {
    }

    public ImagePainter(Image image) {
        this(image, (Effect[]) null);
    }

    public ImagePainter(Image image, Effect... effects) {
        setImage(image);
        setEffects(effects);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        revalidate();
        repaint();
    }

    protected BufferedImage createBuffer(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    protected boolean isPainterValid() {
        return getBuffer() != null;
    }

    protected boolean isBufferValid(Graphics2D g2, JXLayer<V> l) {
        return true;
    }

    protected void revalidate() {
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
            processEffects(null);
        } 
    }
}
