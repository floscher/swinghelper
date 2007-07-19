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

public class BufferedPainter<V extends JComponent>
        extends AbstractBufferedPainter<V> {

    private Painter<V> painter;

    public BufferedPainter() {
        this(new DefaultPainter<V>(), (Effect[]) null);
    }

    public BufferedPainter(Painter<V> painter) {
        this(painter, (Effect[]) null);
    }

    public BufferedPainter(Effect... effects) {
        this(new DefaultPainter<V>(), effects);
    }

    public BufferedPainter(Painter<V> painter, Effect... effects) {
        setDelegatePainter(painter);
        setEffects(effects);
        getModel().setIncrementalUpdate(true);
    }

    public Painter<V> getDelegatePainter() {
        return painter;
    }

    public void setDelegatePainter(Painter<V> painter) {
        if (this.painter != painter) {
            if (this.painter != null) {
                this.painter.removeLayerItemListener(this);
            }
            if (painter != null) {
                painter.addLayerItemListener(this);
            }
            this.painter = painter;
            validate();
            fireLayerItemChanged();
        }
    }

    protected boolean isPainterValid() {
        return getDelegatePainter() != null;
    }

    protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
        getDelegatePainter().paint(g2, l);
    }

    public void update() {
        if (getDelegatePainter() != null) {
            getDelegatePainter().update();
        }
    }
}
