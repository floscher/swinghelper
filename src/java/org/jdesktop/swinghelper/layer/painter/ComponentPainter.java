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

public class ComponentPainter <V extends JComponent>
        extends AbstractBufferedPainter<V> {

    private JComponent component;

    public ComponentPainter() {
        this(null, (Effect<V>[]) null);
    }

    public ComponentPainter(JComponent component) {
        this(component, (Effect<V>[]) null);
    }

    public ComponentPainter(JComponent component, Effect<V>... effects) {
        setComponent(component);
        setEffects(effects);
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
        fireLayerItemChanged();
    }

    public void repaint() {
        if (isPainterValid()) {
            if (getBuffer() == null ||
                    getBuffer().getWidth() != component.getWidth() ||
                    getBuffer().getHeight() != component.getHeight()) {
                setBuffer(createBuffer(component.getWidth(), component.getHeight()));
            }
            Graphics g = getBuffer().getGraphics();
            component.paint(g);
            g.dispose();            
            processEffects(null);
        }
    }

    protected boolean isPainterValid() {
        return component != null &&
                component.getWidth() != 0 && component.getHeight() != 0;
    }

    protected boolean isImageValid(Graphics2D g2, JXLayer<V> l) {
        return true;
    }
    
    protected BufferedImage createBuffer(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
