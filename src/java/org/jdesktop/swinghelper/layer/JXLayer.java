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

import org.jdesktop.swinghelper.layer.item.LayerItemEvent;
import org.jdesktop.swinghelper.layer.item.LayerItemListener;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

public class JXLayer<V extends JComponent> extends JComponent {
    public V view;
    private JComponent glassPane;
    public Painter<V> painter;
    private boolean isPainting;
    private LayerItemListener itemListener;

    // Enabled/disabled support
    private static final FocusTraversalPolicy
            disabledPolicy = new LayoutFocusTraversalPolicy() {
        protected boolean accept(Component aComponent) {
            return false;
        }
    };
    private final static MouseListener nullMouseListener = new MouseAdapter() {
    };
    private Component recentFocusOwner;

    // Constructors
    public JXLayer() {
        this((V) null);
    }

    public JXLayer(V view) {
        this(view, new DefaultPainter<V>());
    }

    public JXLayer(Painter<V> painter) {
        this(null, painter);
    }

    public JXLayer(V view, Painter<V> painter) {
        itemListener = createLayerItemListener();
        setView(view);
        setPainter(painter);
        setGlassPane(new JXGlassPane());
        setLayout(LayerLayout.getSharedInstance());
        setPainter(painter);
        // it doesn't effect until we setFocusTraversalPolicyProvider(true);  
        setFocusTraversalPolicy(disabledPolicy);
    }

    // Main setters and getters
    public V getView() {
        return view;
    }

    public void setView(V view) {
        JComponent oldView = getView();
        if (oldView != null) {
            super.remove(oldView);
        }
        if (view != null) {
            super.addImpl(view, null, getComponentCount());
        }
        this.view = view;
    }

    public JComponent getGlassPane() {
        return glassPane;
    }

    public void setGlassPane(JComponent glassPane) {
        if (glassPane == null) {
            throw new IllegalArgumentException("GlassPane can't be set to null");
        }
        JComponent oldGlassPane = getGlassPane();
        if (oldGlassPane != null) {
            super.remove(oldGlassPane);
        }
        super.addImpl(glassPane, null, 0);
        this.glassPane = glassPane;
    }

    public Painter<V> getPainter() {
        return painter;
    }

    public void setPainter(Painter<V> painter) {
        if (painter == null) {
            throw new IllegalArgumentException("Null painter is not supported; set DefaultPainter");
        }
        Painter<V> oldPainter = getPainter();
        if (painter != oldPainter) {
            if (oldPainter != null) {
                oldPainter.removeLayerItemListener(itemListener);
            }
            painter.addLayerItemListener(itemListener);
        }
        this.painter = painter;
        repaint();
    }

    // add/remove
    protected void addImpl(Component comp, Object constraints, int index) {
        throw new UnsupportedOperationException("JXLayer.add() is not supported; use setView() instead");
    }

    public void remove(Component comp) {
        if (comp == getView()) {
            view = null;
        } else if (comp == getGlassPane()) {
            throw new IllegalArgumentException("GlassPane can't be removed");
        }
        super.remove(comp);
    }

    public void removeAll() {
        setView(null);
    }

    // Painting
    public void paint(Graphics g) {
        if (!isPainting && painter.isEnabled() && g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g.create();
            isPainting = true;
            painter.paint(g2, this);
            isPainting = false;
            g2.dispose();
        } else {
            super.paint(g);
        }
    }

    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public void setBorder(Border border) {
        throw new UnsupportedOperationException("JXLayer.setBorder() is not supported");
    }

    public boolean contains(int x, int y) {
        Painter<V> painter = getPainter();
        if (painter != null && painter.isEnabled()) {
            return painter.contains(x, y, this);
        }
        return super.contains(x, y);
    }

    // ChangeListener    
    private LayerItemListener createLayerItemListener() {
        return new LayerItemListener() {
            public void layerItemChanged(LayerItemEvent e) {
                Rectangle clipBounds = e.getClip() == null ?
                        new Rectangle(getSize()) : e.getClip().getBounds();
                if (view != null) {
                    view.repaint(clipBounds.x, clipBounds.y,
                            clipBounds.width, clipBounds.height);
                } else {
                    repaint(clipBounds.x, clipBounds.y,
                            clipBounds.width, clipBounds.height);
                }
            }
        };
    }

    public void setEnabled(boolean enabled) {
        if (enabled != isEnabled()) {
            if (enabled) {
                getGlassPane().removeMouseListener(nullMouseListener);
                setFocusTraversalPolicyProvider(false);
                boolean isGlassPaneFocused = getGlassPane().isFocusOwner();
                getGlassPane().setFocusable(false);
                if (isGlassPaneFocused && recentFocusOwner != null) {
                    recentFocusOwner.requestFocusInWindow();
                }
                recentFocusOwner = null;
            } else {
                getGlassPane().addMouseListener(nullMouseListener);
                setFocusTraversalPolicyProvider(true);

                KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                Component focusOwner = kfm.getFocusOwner();
                if (focusOwner != null && SwingUtilities.isDescendingFrom(focusOwner, this)) {
                    recentFocusOwner = focusOwner;
                    getGlassPane().setFocusable(true);
                    getGlassPane().requestFocusInWindow();
                }
            }
        }
        super.setEnabled(enabled);
    }
}


