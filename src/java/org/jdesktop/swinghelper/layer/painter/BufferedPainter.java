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

public class BufferedPainter<V extends JComponent>
        extends AbstractBufferedPainter<V> {

    private Painter<V> painter;

    public BufferedPainter() {
        this(null, (Effect<V>[]) null);
    }

    public BufferedPainter(Painter<V> painter) {
        this(painter, (Effect<V>[]) null);
    }

    public BufferedPainter(Painter<V> painter, Effect<V>... effects) {
        setPainter(painter);
        setEffects(effects);
    }

    public Painter<V> getPainter() {
        return painter;
    }
    
    public void setPainter(Painter<V> painter) {
        this.painter = painter;
        fireLayerItemChanged();
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        if (painter != null) {
            configure(g2, l);
            if (getBuffer() == null ||
                    getBuffer().getWidth() != l.getWidth() ||
                    getBuffer().getHeight() != l.getHeight()) {
                setBuffer(createBuffer(g2, l.getWidth(), l.getHeight()));
            }
            Graphics2D bufg = (Graphics2D) getBuffer().getGraphics();
            bufg.setClip(g2.getClip());
            painter.paint(bufg, l);
            processEffects(g2.getClip());
            bufg.dispose();
            g2.drawImage(getBuffer(), 0, 0, null);
        }
    }
    
    protected BufferedImage createBuffer(Graphics2D g2, int width, int height) {
        return g2.getDeviceConfiguration().
                createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }
}
