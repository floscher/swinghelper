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

public class ImagePainter <V extends JComponent>
        extends AbstractBufferedPainter<V> {
    private BufferedImage image;

    protected ImagePainter() {
    }
    
    public ImagePainter(BufferedImage image) {
        this(image, (Effect[]) null);
    }
    
    public ImagePainter(BufferedImage image, Effect... effects) {
        setImage(image);
        setEffects(effects);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        fireLayerItemChanged();
    }

    public boolean isIncrementalUpdate(JXLayer<V> l) {
        return false;
    }
    
    protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
        BufferedImage image = getImage();
        if (image != null) {
            g2.drawImage(image, 0, 0, null);
        }
    }
}
