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
import org.jdesktop.swinghelper.layer.item.LayerItemEvent;
import org.jdesktop.swinghelper.layer.effect.Effect;
import org.jdesktop.swinghelper.layer.painter.model.BufferedPainterModel;
import org.jdesktop.swinghelper.layer.painter.model.DefaultBufferedPainterModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class AbstractBufferedPainter<V extends JComponent>
        extends AbstractPainter<V> {

    private BufferedImage buffer;
    private Effect[] effects = new Effect[0];

    protected AbstractBufferedPainter() {
        this(new DefaultBufferedPainterModel());
    }

    protected AbstractBufferedPainter(BufferedPainterModel model) {
        super(model);
    }

    public BufferedPainterModel getModel() {
        return (BufferedPainterModel) super.getModel();
    }

    /**
     * If set to true the buffer is repainted incrementally,
     * respecting the current clip area
     * otherwise the whole buffer is repainted each time
     * and only if all visible area of the JXLayer is repainted
     */
    public boolean isIncrementalUpdate(JXLayer<V> l) {
        return getModel().isIncrementalUpdate();
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    protected void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

    public void setEffects(Effect... effects) {
        if (effects == null) {
            effects = new Effect[0];
        }
        for (Effect effect : getEffects()) {
            effect.removeLayerItemListener(this);
        }
        this.effects = new Effect[effects.length];
        System.arraycopy(effects, 0, this.effects, 0, effects.length);
        for (Effect effect : effects) {
            effect.addLayerItemListener(this);
        }
        validate();
        fireLayerItemChanged();
    }

    public Effect[] getEffects() {
        Effect[] result = new Effect[effects.length];
        System.arraycopy(effects, 0, result, 0, result.length);
        return result;
    }

    protected void processEffects(Shape clip) {
        if (getBuffer() == null) {
            throw new IllegalStateException("Buffer is null");
        }
        for (Effect e : getEffects()) {
            if (e.isEnabled()) {
                e.apply(getBuffer(), clip);
            }
        }
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        if (isLayerValid(l) && isPainterValid()) {
            // if buffer is not valid it should be repainted
            if (!isBufferValid(g2, l)) {
                // if buffer format is not valid it should be recreated
                if (!isBufferFormatValid(g2, l)) {
                    setBuffer(createBuffer(g2, l.getWidth(), l.getHeight()));
                }
                Graphics2D bufg = (Graphics2D) getBuffer().getGraphics();
                if (isIncrementalUpdate(l)) {
                    bufg.setClip(g2.getClip());
                }
                paintToBuffer(bufg, l);
                processEffects(bufg.getClip());
                bufg.dispose();
            }
            configure(g2, l);
            g2.drawImage(getBuffer(), 0, 0, null);
        }
    }

    protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
    }

    protected boolean isPainterValid() {
        return true;
    }

    protected boolean isBufferFormatValid(Graphics2D g2, JXLayer<V> l) {
        BufferedImage buffer = getBuffer();
        return buffer != null &&
                buffer.getWidth() == l.getWidth() &&
                buffer.getHeight() == l.getHeight();
    }

    protected boolean isBufferValid(Graphics2D g2, JXLayer<V> l) {
        return !isIncrementalUpdate(l) &&
                !l.getVisibleRect().equals(g2.getClipBounds());
    }

    protected boolean isLayerValid(JXLayer<V> l) {
        return l.getWidth() != 0 && l.getHeight() != 0;
    }

    protected BufferedImage createBuffer(Graphics2D g2, int width, int height) {
        return g2.getDeviceConfiguration().
                createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public void layerItemChanged(LayerItemEvent e) {
        validate();
        super.layerItemChanged(e);
    }

    protected void validate() {
    }
}
