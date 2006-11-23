/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
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

import org.jdesktop.swinghelper.layer.painter.Painter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class ComponentPainter extends Painter {
    private Component component;
    private BufferedImage image;

    public ComponentPainter() {
    }

    public ComponentPainter(Component component) {
        setComponent(component);
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
        repaint();
    }

    public void repaint() {
        if (component == null
                || component.getWidth() == 0 || component.getHeight() == 0) {
            return;
        }
        if (image == null || image.getWidth() != component.getWidth()
                || image.getHeight() != component.getHeight()) {
            image = new BufferedImage(component.getWidth(),
                    component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        component.paint(image.getGraphics());
    }

    public void paint(Graphics2D g2, Component c) {
        if (image == null) {
            return;
        }
        g2.drawImage(image, 0, 0, null);
    }
}
