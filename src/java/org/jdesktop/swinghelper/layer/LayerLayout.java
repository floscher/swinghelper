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

package org.jdesktop.swinghelper.layer;

import javax.swing.*;
import java.awt.*;

public class LayerLayout implements LayoutManager {
    private static final LayerLayout sharedInstance = new LayerLayout();

    public static LayerLayout getSharedInstance() {
        return sharedInstance;
    }

    public void layoutContainer(Container parent) {
        JXLayer layer = (JXLayer) parent;
        JComponent view = layer.getView();
        JComponent glassPane = layer.getGlassPane();
        if (view != null) {
            view.setLocation(0, 0);
            view.setSize(layer.getWidth(), layer.getHeight());
        }
        if (glassPane != null) {
            glassPane.setLocation(0, 0);
            glassPane.setSize(layer.getWidth(), layer.getHeight());
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        JXLayer layer = (JXLayer) parent;
        JComponent view = layer.getView();
        if (view != null) {
            return view.getMinimumSize();
        }
        return new Dimension(4, 4);
    }

    public Dimension preferredLayoutSize(Container parent) {
        JXLayer layer = (JXLayer) parent;
        JComponent view = layer.getView();
        if (view != null) {
            return view.getPreferredSize();
        }
        return new Dimension(0, 0);
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }
}
