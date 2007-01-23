/*
 * Copyright (c) 2006 Alexander Potochkin
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

import org.jdesktop.swinghelper.layer.AbstractLayerItem;
import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;

public class ImageOpEffect<V extends JComponent>
        extends AbstractLayerItem implements Effect<V> {
    private BufferedImageOp bufferedImageOp;

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
        fireStateChanged();
    }

    private boolean isInPlace() {
        return !(bufferedImageOp instanceof ConvolveOp);
    }

    public BufferedImage apply(BufferedImage buf, JXLayer<V> l) {
        if (bufferedImageOp != null) {
            if (isInPlace()) {
                return bufferedImageOp.filter(buf, buf);
            }
            BufferedImage dest =
                    bufferedImageOp.createCompatibleDestImage(buf, buf.getColorModel());
            return bufferedImageOp.filter(buf, dest);
        }
        return buf;
    }
}
