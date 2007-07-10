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
import org.jdesktop.swinghelper.layer.item.AbstractLayerItem;
import org.jdesktop.swinghelper.layer.item.LayerItemEvent;
import org.jdesktop.swinghelper.layer.item.LayerItemListener;
import org.jdesktop.swinghelper.layer.item.LayerItem;
import org.jdesktop.swinghelper.layer.painter.model.DefaultPainterModel;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

abstract public class AbstractPainter <V extends JComponent>
        extends AbstractLayerItem implements Painter<V>, LayerItemListener {
    private PainterModel model;
    private List<LayerItem> children;

    protected AbstractPainter() {
        this(new DefaultPainterModel());
    }

    protected AbstractPainter(PainterModel model) {
        if (model == null) {
            throw new IllegalArgumentException("PainterModel is null");
        }
        this.model = model;
        model.addLayerItemListener(this);
        children = new ArrayList<LayerItem>();
    }

    public PainterModel getModel() {
        return model;
    }

    // Painting
    protected void configure(Graphics2D g2, JXLayer<V> l) {
        if (getModel().isEnabled()) {
            applyComposite(g2, getComposite(l));
            applyTransform(g2, getTransform(l));
            applyClip(g2, getClip(l));
            applyRenderingHints(g2, getRenderingHints(l));
        }
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
    }

    public Composite getComposite(JXLayer<V> l) {
        return getModel().getComposite();
    }

    public AffineTransform getTransform(JXLayer<V> l) {
        return getModel().getTransform();
    }

    public Shape getClip(JXLayer<V> l) {
        return getModel().getClip();
    }

    public Map<RenderingHints.Key, Object> getRenderingHints(JXLayer<V> l) {
        return getModel().getRenderingHints();
    }

    protected void applyComposite(Graphics2D g2, Composite composite) {
        if (composite != null) {
            g2.setComposite(composite);
        }
    }

    protected void applyTransform(Graphics2D g2, AffineTransform transform) {
        if (transform != null) {
            g2.transform(transform);
        }
    }

    protected void applyClip(Graphics2D g2, Shape clip) {
        if (clip != null) {
            if (g2.getClip() == null) {
                g2.setClip(clip);
            } else {
                Area clipArea = new Area(g2.getClip());
                clipArea.intersect(new Area(clip));
                g2.setClip(clipArea);
            }
        }
    }

    protected void applyRenderingHints(Graphics2D g2, Map<RenderingHints.Key, Object> hints) {
        if (hints != null) {
            for (RenderingHints.Key key : hints.keySet()) {
                Object value = hints.get(key);
                if (value != null) {
                    g2.setRenderingHint(key, hints.get(key));
                }
            }
        }
    }

    public boolean contains(int x, int y, JXLayer<V> l) {
        Shape clip = getClip(l);
        return clip == null || clip.contains(x, y);
    }

    public void layerItemChanged(LayerItemEvent e) {
        repaint();
    }

    public final void update() {
        updatePainter();
        for (LayerItem item : children) {
            item.update();
        }
        repaint();
    }

    protected void updatePainter() {
    }
    
    protected void addItem(LayerItem item) {
        item.addLayerItemListener(this);
        children.add(item);
    }
    
    protected void removeItem(LayerItem item) {
        item.removeLayerItemListener(this);
        children.remove(item);
    }
}
