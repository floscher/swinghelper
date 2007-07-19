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

import javax.swing.*;
import java.awt.*;

public class CompoundPainter <V extends JComponent>
        extends AbstractPainter<V> {

    private Painter<V>[] painters = new Painter[0];

    public CompoundPainter() {
        this((Painter<V>[]) null);
    }

    public CompoundPainter(Painter<V>... painters) {
        setPainters(painters);
    }

    public Painter<V>[] getPainters() {
        Painter<V>[] result = new Painter[painters.length];
        System.arraycopy(painters, 0, result, 0, result.length);
        return result;
    }

    public void setPainters(Painter<V>... painters) {
        if (painters == null) {
            painters = new Painter[0];
        }
        for (Painter<V> painter : getPainters()) {
            painter.removeLayerItemListener(this);
        }
        this.painters = new Painter[painters.length];
        System.arraycopy(painters, 0, this.painters, 0, painters.length);
        for (Painter<V> painter : painters) {
            painter.addLayerItemListener(this);
        }
        fireLayerItemChanged();
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        super.paint(g2, l);
        for (Painter<V> painter : painters) {
            Graphics2D temp = (Graphics2D) g2.create();
            if (painter.isEnabled()) {
                painter.paint(temp, l);
            }
            temp.dispose();
        }
    }

    public void update() {
        for (Painter<V> painter : painters) {
            painter.update();
        }
    }
}
