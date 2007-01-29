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

import org.jdesktop.swinghelper.layer.effect.Effect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class AbstractBufferedPainter<V extends JComponent>
        extends AbstractPainter<V> {

    private BufferedImage buffer;
    private Effect[] effects = new Effect[0];

    public BufferedImage getBuffer() {
        return buffer;
    }

    protected void setBuffer(BufferedImage buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("BufferedImage is null");
        }
        this.buffer = buffer;
    }

    public void setEffects(Effect<V>... effects) {
        if (effects == null) {
            effects = new Effect[0];
        }
        for (Effect<V> effect : getEffects()) {
            effect.removeLayerItemListener(getHandler());
        }
        this.effects = new Effect[effects.length];
        System.arraycopy(effects, 0, this.effects, 0, effects.length);
        for (Effect<V> effect : effects) {
            effect.addLayerItemListener(getHandler());
        }
        fireLayerItemChanged();
    }

    public Effect<V>[] getEffects() {
        Effect<V>[] result = new Effect[effects.length];
        System.arraycopy(effects, 0, result, 0, result.length);
        return result;
    }

    protected void processEffects(Shape clip) {
        if (getBuffer() == null) {
            throw new IllegalStateException("Buffer is null");
        }
        for (Effect<V> e : getEffects()) {
            if (e.isEnabled()) {
                e.apply(getBuffer(), clip);
            }
        }
    }
}
