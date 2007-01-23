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

package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.AbstractLayerItem;
import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.configurator.Configurator;
import org.jdesktop.swinghelper.layer.painter.configurator.DefaultConfigurator;
import org.jdesktop.swinghelper.layer.painter.model.DefaultPainterModel;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Area;
import java.util.Map;

public abstract class AbstractPainter <V extends JComponent> 
        extends AbstractLayerItem implements Painter<V> {
    private PainterModel model;
    private Configurator<V> configurator;
    private Handler handler;

    protected AbstractPainter() {
        setModel(new DefaultPainterModel());
        setConfigurator(new DefaultConfigurator<V>());
    }

    public PainterModel getModel() {
        return model;
    }

    public void setModel(PainterModel model) {
        if (model == null) {
            throw new IllegalArgumentException(
                    "Null model is not supported; set DefaultPainterModel");
        }
        PainterModel oldModel = getModel();

        if (model != oldModel) {
            if (oldModel != null) {
                oldModel.removeChangeListener(getHandler());
            }
            model.addChangeListener(getHandler());
            this.model = model;
            fireStateChanged();
        }
    }

    public Configurator<V> getConfigurator() {
        return configurator;
    }

    public void setConfigurator(Configurator<V> configurator) {
        if (configurator == null) {
            throw new IllegalArgumentException(
                    "Null configurator is not supported; set DefaultConfigurator");
        }
        Configurator oldConfigurator = getConfigurator();
        if (configurator != oldConfigurator) {
            if (oldConfigurator != null) {
                oldConfigurator.removeChangeListener(getHandler());
            }
            configurator.addChangeListener(getHandler());
        }
        this.configurator = configurator;
        fireStateChanged();
    }

    protected void configure(Graphics2D g2, JXLayer<V> l) {
        if (getModel().isEnabled()) {
            applyModel(g2);
        }
        if (getConfigurator().isEnabled()) {
            applyConfigurator(g2, l);
        } 
    }

    private void applyModel(Graphics2D g2) {
        if (getModel().getComposite() != null) {
            g2.setComposite(getModel().getComposite());
        }
        if (getModel().getTransform() != null) {
            g2.transform(getModel().getTransform());
        }
        applyClip(g2, getModel().getClip());
        applyRenderingHints(g2, getModel().getRenderingHints());
    }
    
    private void applyConfigurator(Graphics2D g2, JXLayer<V> l) {
        if (getConfigurator().getComposite(l) != null) {
            g2.setComposite(getConfigurator().getComposite(l));
        }
        if (getConfigurator().getTransform(l) != null) {
            g2.transform(getConfigurator().getTransform(l));
        }
        applyClip(g2, getConfigurator().getClip(l));
        applyRenderingHints(g2, getConfigurator().getRenderingHints(l));
    }
    
    private void applyClip(Graphics2D g2, Shape clip) {
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

    private void applyRenderingHints(Graphics2D g2, Map<RenderingHints.Key, Object> hints) {
        if (hints != null) {
            for (RenderingHints.Key key : hints.keySet()) {
                Object value = hints.get(key);
                if (value != null) {
                    g2.setRenderingHint(key, hints.get(key));
                }
            }
        }
    }
    
    public abstract void paint(Graphics2D g2, JXLayer<V> l);

    public void repaint(JXLayer<V> l) {
    }

    protected ChangeListener getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    private class Handler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }
}
