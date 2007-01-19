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
