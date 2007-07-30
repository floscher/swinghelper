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
import org.jdesktop.swinghelper.layer.painter.model.DefaultPainterModel;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;
import org.jdesktop.swinghelper.layer.item.LayerItem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

/**
 * The default implementation of the {@link Painter} interface,
 * in the most usual case the only {@link #paint(Graphics2D,JXLayer)} is to be overriden.
 * <p/>
 * The {@link Painter} interface is designed to support a single painter for multiple {@link JXLayer} objects,
 * so it is a rare situation when a painter really needs to have a link to its layer.
 * If you want an {@link AbstractPainter} to repaint all {@link JXLayer}s it is used for,
 * call {@link AbstractPainter#fireLayerItemChanged()} after a painter's state has been changed
 * <p/>
 * For a new public state of the painter the following pattern should be used:
 * <p/>
 * <pre>
 * public class ColorPainter<V extends JComponent> extends AbstractPainter<V> {
 *   // The color we want to fill the layer with
 *   private Color color;<br>
 *   // A public method which affects the painting
 *   public void setColor(Color color) {
 *       this.color = color;
 *       // This will repaint the layer
 *       fireLayerItemChanged();
 *   }<br>
 *   public void paint(Graphics2D g2, JXLayer<V> l) {
 *       // This will configure g2 with current settings
 *       super.paint(g2, l);
 *       // The custom painting is performed here
 *       g2.setColor(this.color);
 *       g2.fillRect(0, 0, l.getWidth(), l.getHeight());
 *   }
 *  }
 * </pre>
 * This class stores all its states in its {@link PainterModel},
 * it also has a hook method which corresponds to the each state from the model
 * <p/>
 * For example:
 * if you need to set a fixed clipping shape you should update the painter's model:
 * <pre>
 * painter.getModel().setClip(new Ellipse2D.Float(0, 0, 100, 100));
 * </pre>
 * but if you'd like the clip to be updated according to the {@link JXLayer},
 * you should override the corresponding {@link #getClip(JXLayer)} method:
 * <pre>
 * public class OvalPainter<V extends JComponent> extends AbstractPainter<V> {
 *   // Ellipsoid clip shape which respects the current layers' size
 *   public Shape getClip(JXLayer<V> l) {
 *       return new Ellipse2D.Float(0, 0, l.getWidth(), l.getHeight());
 *   }
 * </pre>
 */
abstract public class AbstractPainter<V extends JComponent>
        extends AbstractLayerItem implements Painter<V>, LayerItemListener {
    private PainterModel model;

    /**
     * Creates a new {@link AbstractPainter}
     * with a new {@link DefaultPainterModel} as its model
     */
    protected AbstractPainter() {
        this(new DefaultPainterModel());
    }

    /**
     * Creates a new {@link AbstractPainter}
     * with a specified painter model
     *
     * @param model {@link PainterModel} to be used for this painter
     */
    protected AbstractPainter(PainterModel model) {
        if (model == null) {
            throw new IllegalArgumentException("PainterModel is null");
        }
        this.model = model;
        model.addLayerItemListener(this);
    }

    /**
     * Gets the {@link PainterModel} of this painter
     * <p/>
     * Note: this method never returns <code>null</code>
     *
     * @return the {@link PainterModel} of this painter
     */
    public PainterModel getModel() {
        return model;
    }

    /**
     * Set up the {@link Graphics2D} instance before painting
     *
     * @param g2 the {@link Graphics2D} instance to paint on
     * @param l  the {@link JXLayer} to paint for
     * @see #getComposite(JXLayer)
     * @see #getTransform(JXLayer)
     * @see #getClip(JXLayer)
     * @see #getRenderingHints(JXLayer)
     */
    protected void configure(Graphics2D g2, JXLayer<V> l) {
        Composite composite = getComposite(l);
        if (composite != null) {
            g2.setComposite(composite);
        }
        Shape clip = getClip(l);
        if (clip != null) {
            g2.clip(clip);
        }
        AffineTransform transform = getTransform(l);
        if (transform != null) {
            g2.transform(transform);
        }
        Map<RenderingHints.Key, Object> hints = getRenderingHints(l);
        if (hints != null) {
            for (RenderingHints.Key key : hints.keySet()) {
                Object value = hints.get(key);
                if (value != null) {
                    g2.setRenderingHint(key, hints.get(key));
                }
            }
        }
    }

    /**
     * The default implementation of the {@link Painter#paint(Graphics2D,JXLayer)} method,
     * it only set up the {@link Graphics2D} instance
     *
     * @param g2 the {@link Graphics2D} instance to paint on
     * @param l  the {@link JXLayer} to paint for
     * @see #configure(Graphics2D,JXLayer<V>)
     */
    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
    }

    /**
     * Gets the {@link Composite} to be used during painting for this {@link JXLayer};
     * the default implementation reads it from the painter's model
     *
     * @param l the {@link JXLayer} to paint for
     * @return the {@link Composite} to be used during painting
     * @see #getModel()
     * @see #paint(Graphics2D,JXLayer)
     */
    protected Composite getComposite(JXLayer<V> l) {
        return getModel().getComposite();
    }

    /**
     * Gets the {@link AffineTransform} to be used during painting for this {@link JXLayer};
     * the default implementation reads it from the painter's model
     *
     * @param l the {@link JXLayer} to paint for
     * @return the {@link AffineTransform} to be used during painting
     * @see #getModel()
     * @see #paint(Graphics2D,JXLayer)
     */
    protected AffineTransform getTransform(JXLayer<V> l) {
        return getModel().getTransform();
    }

    /**
     * Gets the clipping {@link Shape} to be used during painting for this {@link JXLayer};
     * the default implementation reads it from the painter's model
     *
     * @param l the {@link JXLayer} to paint for
     * @return the clipping {@link Shape} to be used during painting
     * @see #getModel()
     * @see #paint(Graphics2D,JXLayer)
     */
    protected Shape getClip(JXLayer<V> l) {
        return getModel().getClip();
    }

    /**
     * Gets the map of the {@link RenderingHints} to be used during painting for this {@link JXLayer};
     * the default implementation reads it from the painter's model
     *
     * @param l the {@link JXLayer} to paint for
     * @return the map of the {@link RenderingHints} to be used during painting
     * @see #getModel()
     * @see #paint(Graphics2D,JXLayer)
     */
    protected Map<RenderingHints.Key, Object> getRenderingHints(JXLayer<V> l) {
        return getModel().getRenderingHints();
    }

    /**
     * The default implementation of the {@link Painter#contains(int,int,JXLayer)} method;
     * It calls the {@link #getClip(JXLayer)} and returns <code>true</code> if the clipping shape
     * contains the (x, y) point and returns <code>false</code> otherwise
     *
     * @param x the <i>x</i> coordinate of the point
     * @param y the <i>y</i> coordinate of the point
     * @param l the {@link JXLayer} this point to be checked for
     * @return <code>true</code> if <code>MouseEvent</code>s are acceped in this point,
     *         <code>false</code> otherwise
     */
    public boolean contains(int x, int y, JXLayer<V> l) {
        Shape clip = getClip(l);
        return clip == null || clip.contains(x, y);
    }

    /**
     * The default implementation of the {@link LayerItemListener#layerItemChanged(LayerItemEvent)};
     * this method listens to the changes of the {@link PainterModel}
     * and all others child {@link LayerItem}s connected with this painter, if any
     *
     * @param e a LayerItemEvent object
     */
    public void layerItemChanged(LayerItemEvent e) {
        fireLayerItemChanged();
    }
}
