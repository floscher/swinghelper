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

/**
 * The default layout manager for the {@link JXLayer}.<br/>
 * It places the glassPane on top of the view component and makes it the same size as {@link JXLayer},
 * it also makes the view component the same size but minus layer's insets<br/>
 *
 * @see JXLayer#getView()
 * @see JXLayer#getGlassPane()
 */
public class LayerLayout implements LayoutManager {
    private static final LayerLayout sharedInstance = new LayerLayout();

    /**
     * The shared instance of {@link LayerLayout}
     *
     * @return the shared instance of {@link LayerLayout}
     */
    public static LayerLayout getSharedInstance() {
        return sharedInstance;
    }

    /**
     * {@inheritDoc}
     */
    public void layoutContainer(Container parent) {
        JXLayer layer = (JXLayer) parent;
        JComponent view = layer.getView();
        JComponent glassPane = layer.getGlassPane();
        if (view != null) {
            Insets insets = layer.getInsets();
            view.setLocation(insets.left, insets.top);
            view.setSize(layer.getWidth() - insets.left - insets.right,
                    layer.getHeight() - insets.top - insets.bottom);
        }
        if (glassPane != null) {
            glassPane.setLocation(0, 0);
            glassPane.setSize(layer.getWidth(), layer.getHeight());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Dimension minimumLayoutSize(Container parent) {
        JXLayer layer = (JXLayer) parent;
        Insets insets = layer.getInsets();
        Dimension ret = new Dimension(insets.left + insets.right,
                insets.top + insets.bottom);
        JComponent view = layer.getView();
        if (view != null) {
            Dimension size = view.getMinimumSize();
            ret.width += size.width;
            ret.height += size.height;
        }
        if (ret.width == 0 || ret.height == 0) {
            ret.width = ret.height = 4;
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public Dimension preferredLayoutSize(Container parent) {
        JXLayer layer = (JXLayer) parent;
        Insets insets = layer.getInsets();
        Dimension ret = new Dimension(insets.left + insets.right,
                insets.top + insets.bottom);
        JComponent view = layer.getView();
        if (view != null) {
            Dimension size = view.getPreferredSize();
            ret.width += size.width;
            ret.height += size.height;
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * {@inheritDoc}
     */
    public void removeLayoutComponent(Component comp) {
    }
}
