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

import org.jdesktop.swinghelper.layer.item.*;
import org.jdesktop.swinghelper.layer.painter.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

/**
 * The universal decorator for Swing components.
 * <p/>
 * {@link JXLayer} is a component wrapper, like a {@link JScrollPane},
 * which provides some useful functionality.
 * You can set a {@link Painter} to the layer and modify its visual appearance
 * and filter mouse events depending on the state of the wrapped component,
 * see http://weblogs.java.net/blog/alexfromsun/archive/2006/12/advanced_painti_2.html
 * and all JXLayer demos from https://swinghelper.dev.java.net/
 * <p/>
 * With {@link JXLayer} it is also very easy to disable a container with
 * all its child components see: 
 * <p/>
 * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2007/06/_enablingdisabl_1.html">
 * Enabling/Disabling Swing Containers</a>
 * <p/>
 * Here is a simple example how to decorate a button
 * and change its visual appearance for the rollover state
 * <p/>
 * <pre>
 *       JButton button = new JButton("Decorate me !");
 *       Painter<AbstractButton> customPainter = new DefaultPainter<AbstractButton>() {
 *           public void paint(Graphics2D g2, JXLayer<AbstractButton> l) {
 *               // paints the layer as is
 *               super.paint(g2, l);
 *               // check the button's state
 *               if (l.getView().getModel().isRollover()) {
 *                   // fill the button with translucent green when rollovered
 *                   g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
 *                   g2.setColor(Color.GREEN);
 *                   g2.fillRect(0, 0, l.getWidth(), l.getHeight());
 *               }
 *           }
 *       };
 *       // create a JXLayer with button and custom painter
 *       JXLayer<AbstractButton> l = new JXLayer<AbstractButton>(button, customPainter);
 *       // add it to a frame or any other container as usual
 *       frame.add(l);
 * </pre>
 * <p/>
 * Note: JXLayer doesn't use glassPane from the top level frame,
 * it has its own transparent panel on the top
 * <p/>
 * Note: JXLayer doesn't install any custom {@link RepaintManager}
 * nor change any state of its child components
 * <p/>
 * Note: If you want to have a translucent or transparent JXLayer,
 * you need to wrap any of its parent with another JXLayer
 * for more details, please see
 * <p/>
 * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2006/12/advanced_painti_3.html">
 * Advanced painting IV - translucency and non rectangular components</a>
 *
 * @see Painter
 * @see AbstractPainter
 */
public class JXLayer<V extends JComponent> extends JComponent {
    public V view;
    private JComponent glassPane;
    public Painter<V> painter;
    private boolean isPainting;
    private LayerItemListener itemListener;

    // Enabled/disabled support
    private final FocusTraversalPolicy
            disabledPolicy = new LayoutFocusTraversalPolicy() {
        protected boolean accept(Component component) {
            return component == getGlassPane();
        }
    };

    private final static FocusListener
            glassPaneFocusListener = new FocusListener() {
        public void focusGained(FocusEvent e) {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e) {
        }
    };

    private final static MouseListener
            emptyMouseListener = new MouseAdapter() {
    };

    private Component recentFocusOwner;
    private boolean enabled = true;

    /**
     * Creates a new {@link JXLayer} 
     * with <code>null</code> view
     */
    public JXLayer() {
        this((V) null);
    }

    /**
     * Creates a new {@link JXLayer}
     * with the given <code>view</code> component
     *  
     * @param view the component to be wrapped 
     */
    public JXLayer(V view) {
        this(view, new DefaultPainter<V>());
    }

    /**
     * Creates a new {@link JXLayer}
     * with the given <code>painter</code> and <code>null</code> view
     *  
     * @param painter the painter to be used for rendering 
     */
    public JXLayer(Painter<V> painter) {
        this(null, painter);
    }

    /**
     * Creates a new {@link JXLayer}
     * with the given <code>view</code> and <code>painter</code> 
     *  
     * @param view the component to be wrapped
     * @param painter the painter to be used for rendering
     */
    public JXLayer(V view, Painter<V> painter) {
        itemListener = createLayerItemListener();
        setView(view);
        setPainter(painter);
        setGlassPane(new JXGlassPane());
        setLayout(LayerLayout.getSharedInstance());
        setPainter(painter);
        setOpaque(true);
        // it doesn't effect until we setFocusTraversalPolicyProvider(true);  
        setFocusTraversalPolicy(disabledPolicy);
    }

    /**
     * Gets the view (wrapped component) for this layer
     * 
     * @return the view (wrapped component) for this layer 
     */
    public V getView() {
        return view;
    }


    /**
     * Sets the view (wrapped component) for this layer
     *  
     * @param view the view (wrapped component) for this layer
     */
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

    /**
     * Gets the glassPane of this layer
     * 
     * @return the glassPane of this layer
     */
    public JComponent getGlassPane() {
        return glassPane;
    }

    /**
     * Sets the glassPane of this layer
     * 
     * @param glassPane the glassPane of this layer
     */
    public void setGlassPane(JComponent glassPane) {
        if (glassPane == null) {
            throw new IllegalArgumentException("GlassPane can't be set to null");
        }
        JComponent oldGlassPane = getGlassPane();
        if (oldGlassPane != null) {
            super.remove(oldGlassPane);
            oldGlassPane.removeFocusListener(glassPaneFocusListener);
        }
        super.addImpl(glassPane, null, 0);
        glassPane.addFocusListener(glassPaneFocusListener);
        this.glassPane = glassPane;
    }

    /**
     * Gets the {@link Painter} of this layer
     * 
     * @return the {@link Painter} of this layer
     */
    public Painter<V> getPainter() {
        return painter;
    }

    /**
     * Sets the {@link Painter} for this layer
     *  
     * @param painter the {@link Painter} for this layer
     * 
     * @see Painter
     * @see AbstractPainter
     */
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

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public void setBorder(Border border) {
        throw new UnsupportedOperationException("JXLayer.setBorder() is not supported");
    }

    /**
     * Checks whether the {@link Painter} of this <code>JXLayer</code> accepts <code>MouseEvent</code>s
     * at the specified point or not, where <code>x</code> and <code>y</code> are defined to be
     * relative to the coordinate system of this component.
     * 
     * @see Painter#contains(int, int, JXLayer) 
     */
    public boolean contains(int x, int y) {
        Painter<V> painter = getPainter();
        if (painter != null && painter.isEnabled()) {
            return super.contains(x, y) && painter.contains(x, y, this);
        }
        return super.contains(x, y);
    }

    // ChangeListener    
    private LayerItemListener createLayerItemListener() {
        return new LayerItemListener() {
            public void layerItemChanged(LayerItemEvent e) {
                if (view != null) {
                    view.repaint();
                } else {
                    repaint();
                }
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(boolean enabled) {
        boolean oldEnabled = isEnabled();
        if (enabled != oldEnabled) {
            if (enabled) {
                getGlassPane().removeMouseListener(emptyMouseListener);
                setFocusTraversalPolicyProvider(false);
                boolean isGlassPaneFocused = getGlassPane().isFocusOwner();
                if (isGlassPaneFocused && recentFocusOwner != null) {
                    recentFocusOwner.requestFocusInWindow();
                }
                recentFocusOwner = null;
            } else {
                getGlassPane().addMouseListener(emptyMouseListener);
                setFocusTraversalPolicyProvider(true);
                KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                Component focusOwner = kfm.getFocusOwner();
                if (focusOwner != null && SwingUtilities.isDescendingFrom(focusOwner, this)) {
                    recentFocusOwner = focusOwner;
                    getGlassPane().requestFocusInWindow();
                }
            }
            this.enabled = enabled;
            firePropertyChange("enabled", oldEnabled, enabled);
            repaint();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return enabled;
    }
}


