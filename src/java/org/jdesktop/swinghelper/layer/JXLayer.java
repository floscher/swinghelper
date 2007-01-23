/*
 * Copyright (c) 2006 Alexander Potochkin
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

import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;
import org.jdesktop.swinghelper.layer.shaper.DefaultShaper;
import org.jdesktop.swinghelper.layer.shaper.Shaper;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class JXLayer <V extends JComponent> extends JComponent {
    public V view;
    private Shaper<V> mouseClipShaper;
    private JComponent glassPane;
    public Painter<V> painter;
    private boolean isPainting;
    private ChangeListener changeListener;

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
        changeListener = createChangeListener();
        setView(view);
        setPainter(painter);
        setGlassPane(new JXGlassPane());
        setLayout(LayerLayout.getSharedInstance());
        setPainter(painter);
        setMouseClipShaper(new DefaultShaper<V>());
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
                oldPainter.removeChangeListener(changeListener);
            }
            painter.addChangeListener(changeListener);
        }
        this.painter = painter;
        repaint();
    }

    public Shaper<V> getMouseClipShaper() {
        return mouseClipShaper;
    }

    public void setMouseClipShaper(Shaper<V> mouseClipShaper) {
        if (mouseClipShaper == null) {
            throw new IllegalArgumentException("Null shaper is not supported; set NullShaper");
        }
        Shaper<V> oldShaper = getMouseClipShaper();
        if (mouseClipShaper != oldShaper) {
            if (oldShaper != null) {
                oldShaper.removeChangeListener(changeListener);
            }
            mouseClipShaper.addChangeListener(changeListener);
        }
        this.mouseClipShaper = mouseClipShaper;
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
        if (border != null) {
            throw new IllegalArgumentException("JXLayer.setBorder() is not supported");
        }
    }

    public boolean contains(int x, int y) {
        Shaper<V> mouseShaper = getMouseClipShaper();
        if (mouseShaper.isEnabled()) {
            return mouseShaper.contains(x, y, this);
        }
        return super.contains(x, y);
    }

    // ChangeListener    
    private ChangeListener createChangeListener() {
        return new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (view != null) {
                    view.repaint();
                }
            }
        };
    }
}


