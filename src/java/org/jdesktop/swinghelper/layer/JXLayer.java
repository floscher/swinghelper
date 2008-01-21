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
import java.awt.*;
import java.awt.event.*;

/**
 * The universal decorator for Swing components<br/>
 * which supports various advanced painting effects<p/>
 * JXLayer is a component wrapper, like a {@link JScrollPane}.<br/>
 * You can set a {@link Painter} to the layer and modify its visual appearance
 * and filter mouse events depending on the state of the wrapped component<p/>
 * for more details, please see JXLayer demos from {@link org.jdesktop.swinghelper.layer.demo}<br/>
 * and <a href="http://weblogs.java.net/blog/alexfromsun/archive/2006/12/advanced_painti_2.html">Advanced painting III - playing with painters</a>
 * <p/>
 * With JXLayer it is also very easy to disable a container with all its child components:<br>
 * please see {@link org.jdesktop.swinghelper.layer.demo.LockedLayerDemo} and 
 * <p/>
 * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2007/06/_enablingdisabl_1.html">
 * Enabling/Disabling Swing Containers</a>
 * <p/>
 * Here is a simple example how to decorate a button
 * and change its visual appearance for the rollover state
 * <p/>
 * <pre>
 *       JButton button = new JButton("Decorate me !");
 *       Painter&lt;AbstractButton&gt; customPainter = new DefaultPainter&lt;AbstractButton&gt;() {
 *           public void paint(Graphics2D g2, JXLayer&lt;AbstractButton&gt; l) {
 *               // DefaultPainter.paint(g2, l) includes painting the layer as is
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
 *       JXLayer&lt;AbstractButton&gt; l = new JXLayer&lt;AbstractButton&gt;(button, customPainter);
 *       // add it to a frame or any other container as usual
 *       frame.add(l);
 * </pre>
 * please see also {@link org.jdesktop.swinghelper.layer.demo.DelegateDemo}
 * <p/>
 * <strong>Note:</strong> JXLayer is very friendly to your application<br/>
 * it doesn't exploit the glassPane from the top level frame, because it has its own one<br/>
 * it also doesn't install any custom {@link RepaintManager} nor change any state of its child components
 * <p/>
 * If you want to have a translucent or transparent JXLayer,
 * you need to wrap any of its parent with another JXLayer,<br/>
 * for more details, please see
 * <br/>
 * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2006/12/advanced_painti_3.html">
 * Advanced painting IV - translucency and non rectangular components</a>
 * <p/>
 * Here you can find about some useful details about using {@link javax.swing.border.Border} with JXLayer
 * <br/>
 * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2007/08/validation_over.html">
 * Validation overlays with JXLayer - the followup</a>
 *  
 * @see Painter
 * @see AbstractPainter
 * @see AbstractBufferedPainter
 * @see BufferedPainter
 * @see org.jdesktop.swinghelper.layer.demo
 */
public class JXLayer<V extends JComponent> extends JComponent {
    private V view;
    private JComponent glassPane;
    private Painter<V> painter;
    private boolean isPainting;
    private LayerItemListener itemListener;

    // Locking/unlocking support
    private final static FocusListener
            layerFocusListener = new FocusListener() {
        public void focusGained(FocusEvent e) {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e) {
        }
    };

    private Component recentFocusOwner;
    private boolean locked;
    private Cursor lockedCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

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
     * @param view    the component to be wrapped
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
        addFocusListener(layerFocusListener);
    }

    /**
     * Gets the view (wrapped component) for this layer<br/>
     * <strong>Note:</strong> this method <strong>may return</strong> <code>null</code>
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
     * Gets the glassPane of this layer<br/>
     * <strong>Note:</strong> this method never returns <code>null</code>
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
        }
        super.addImpl(glassPane, null, 0);
        this.glassPane = glassPane;
    }

    /**
     * Gets the {@link Painter} of this layer<br/>
     * <strong>Note:</strong> this method never returns <code>null</code>
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
     * @throws IllegalArgumentException if <code>painter</code> is <code>null</code>
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

    /**
     * {@link JXLayer} supports only two child components:
     * the view and the glassPane, which can be set with help of the
     * corresponding methods
     *
     * @throws UnsupportedOperationException this method is not supported
     * @see #setView(JComponent)
     * @see #setGlassPane(JComponent)
     */
    protected void addImpl(Component comp, Object constraints, int index) {
        throw new UnsupportedOperationException("JXLayer.add() is not supported; use setView() instead");
    }

    /**
     * Removes the view from the JXLayer,
     * the glassPane can't be removed
     *
     * @param comp component to be removed
     * @throws IllegalArgumentException if <code>comp</code> is equal to layer's glassPane
     * @see #getView()
     * @see #setView(JComponent)
     * @see #getGlassPane()
     * @see #setGlassPane(JComponent)
     */
    public void remove(Component comp) {
        if (comp == getView()) {
            view = null;
        } else if (comp == getGlassPane()) {
            throw new IllegalArgumentException("GlassPane can't be removed");
        }
        super.remove(comp);
    }

    /**
     * Removes the view from this JXLayer.
     *
     * @see #getView()
     * @see #setView(JComponent)
     */
    public void removeAll() {
        setView(null);
    }

    /**
     * Delegates all painting to the {@link Painter},
     * which was set with {@link #setPainter(Painter)} method.<br>
     * It happens only if {@link Painter#isEnabled()} returns <code>true</code>
     * and <code>g</code> is instance of <code>Graphics2D</code><br>
     * otherwise the super implementation is called.
     *
     * @param g the {@link Graphics} to render to
     */
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

    /**
     * If layer is opaque, fills it with the background color
     *
     * @param g the {@link Graphics} to render to
     * @see #isOpaque()
     * @see #getBackground()
     */
    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * This method always returns <code>false</code>
     * to support the view and the glassPane overlap
     *
     * @return <code>false</code>
     */
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    /**
     * Checks whether the {@link Painter} of this <code>JXLayer</code> accepts <code>MouseEvent</code>s
     * at the specified point or not, where <code>x</code> and <code>y</code> are defined to be
     * relative to the coordinate system of this component.
     *
     * @see Painter#contains(int,int,JXLayer)
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
                repaint();
            }
        };
    }

    /**
     * Sets if this layer is locked or not.
     * The locked layer doesn't pass any mouseEvents to its view component
     * and exclude the view component and all its subcomponents
     * from the layer's {@link java.awt.FocusTraversalPolicy}
     * which makes it impossible to traverse focus to those components
     * with the Tab key
     * <p/>
     * With this method you have an efficient way
     * to temporary disable the layer's content.
     * For more info please see
     * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2007/06/_enablingdisabl_1.html">
     * Enabling/Disabling Swing Container<a/>
     *
     * @param locked <code>true</code> if this layer should be locked
     *               <code>false</code> otherwise
     * 
     * @see #setLockedCursor(java.awt.Cursor) 
     */
    public void setLocked(boolean locked) {
        if (locked != isLocked()) {
            Component focusOwner =
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
            boolean isFocusInsideLayer =
                    focusOwner != null && SwingUtilities.isDescendingFrom(focusOwner, this);
            if (locked) {
                getView().setVisible(false);
                if (isFocusInsideLayer) {
                    recentFocusOwner = focusOwner;
                    requestFocusInWindow();
                }
                getGlassPane().setCursor(getLockedCursor());
            } else {
                getView().setVisible(true);
                if (isFocusInsideLayer && recentFocusOwner != null) {
                    recentFocusOwner.requestFocusInWindow();
                }
                recentFocusOwner = null;
                getGlassPane().setCursor(null);
            }
            this.locked = locked;
            repaint();
        }
    }

    /**
     * Returns <code>true</code> is this layer is locked,
     * <code>false</code> otherwise
     * 
     * @return <code>true</code> is this layer is locked,
     * <code>false</code> otherwise
     * 
     * @see #setLocked(boolean) 
     * @see #setLockedCursor(java.awt.Cursor) 
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Returns the mouse cursor to be used for the locked layer,
     * it is a <code>Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)</code> by default
     * 
     * @return the cursor to be used for the locked layer
     * 
     * @see #setLocked(boolean) 
     */
    public Cursor getLockedCursor() {
        return lockedCursor;
    }

    /**
     * Sets the mouse cursor to be used for tor the locked layer
     * 
     * @param lockedCursor the mouse cursor to be used for tor the locked layer
     * 
     * @see #setLocked(boolean)  
     */
    public void setLockedCursor(Cursor lockedCursor) {
        this.lockedCursor = lockedCursor;
        if (isLocked()) {
            getGlassPane().setCursor(lockedCursor);
        }
    }
}


