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

package org.jdesktop.swinghelper.layer;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class JXLayer extends JXContainer {
    private Container contentPane;
    private Component glassPane;

    public JXLayer() {
        this(true);
    }

    public JXLayer(boolean isAdvancedPaintingEnabled) {
        this(new FlowLayout(), isAdvancedPaintingEnabled);
    }

    public JXLayer(Container c) {
        this(c, true);
    }
    
    public JXLayer(Container c, boolean isAdvancedPaintingEnabled) {
        this((LayoutManager) null, isAdvancedPaintingEnabled);
        setContentPane(c);
    }
    
    public JXLayer(LayoutManager layout) {
        this(layout, true);
    }

    public JXLayer(LayoutManager layout, boolean isAdvancedPaintingEnabled) {
        super(null);
        setContentPane(new JPanel());
        setGlassPane(new JXGlassPane());
        getContentPane().setLayout(layout);
        setAdvancedPaintingEnabled(isAdvancedPaintingEnabled);
    }
    
    
    public void doLayout() {
        if (contentPane != null) {
            setPreferredSize(contentPane.getPreferredSize());
            contentPane.setLocation(0, 0);
            contentPane.setSize(getWidth(), getHeight());
        }
        if (glassPane != null) {
            glassPane.setLocation(0, 0);
            glassPane.setSize(getWidth(), getHeight());
        }
    }

    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        contentPane.add(comp, constraints, index);
        doLayout();
    }

    public void remove(Component comp) {
        contentPane.remove(comp);
    }

    public void removeAll() {
        contentPane.removeAll();
    }

    public void setLayout(LayoutManager mgr) {
        if (contentPane != null) {
            contentPane.setLayout(mgr);
        }
    }
    
    public LayoutManager getLayout() {
        return contentPane.getLayout();
    }

    public Container getContentPane() {
        return contentPane;
    }

    public void setContentPane(Container contentPane) {
        if (contentPane == null) {
            throw new IllegalArgumentException("ContentPane can't be set to null");
        }
        if (getContentPane() != null) {
            super.remove(contentPane);
        }
        super.addImpl(contentPane, null, getComponentCount());
        this.contentPane = contentPane;
    }

    public Component getGlassPane() {
        return glassPane;
    }

    public void setGlassPane(Component glassPane) {
        if (glassPane == null) {
            throw new IllegalArgumentException("GlassPane can't be set to null");
        }
        if (getGlassPane() != null) {
            super.remove(glassPane);
        }
        super.addImpl(glassPane, null, 0);
        this.glassPane = glassPane;
    }

    protected Component getPaintingDelegate() {
        return getContentPane();
    }

    public void setPreferredSize(Dimension preferredSize) {
        contentPane.setPreferredSize(preferredSize);
    }

    public Dimension getPreferredSize() {
        return contentPane.getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return contentPane.getMaximumSize();
    }

    public void setMaximumSize(Dimension maximumSize) {
        contentPane.setMaximumSize(maximumSize);
    }

    public Dimension getMinimumSize() {
        return contentPane.getMinimumSize();
    }

    public void setMinimumSize(Dimension minimumSize) {
        contentPane.setMinimumSize(minimumSize);
    }
}
