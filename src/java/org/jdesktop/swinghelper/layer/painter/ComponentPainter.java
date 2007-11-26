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

import org.jdesktop.swinghelper.layer.effect.Effect;
import org.jdesktop.swinghelper.layer.item.*;

import javax.swing.*;
import java.awt.*;

/**
 * The implementation of the {@link ImagePainter}
 * which is designed to paint screenshots of Swing components;<br/>
 * this painter can be used to create animation effects
 * like fade in and fade out of a component
 * <p/> 
 * <strong>Note:</strong> 
 * call {@link #update()} to take the up-to-date screenshot of the component,<br/>
 * this method must be called <b>outside</b> any paint methods<br/> 
 * (e.g. any listener or is a good place to put {@link #update()} into)   
 * 
 * @see #getComponent() 
 * @see #setComponent(JComponent)
 * @see #update() 
 */
public class ComponentPainter <V extends JComponent>
        extends ImagePainter<V> {

    private JComponent component;

    /**
     * Creates a new {@link ComponentPainter}
     */
    public ComponentPainter() {
        this(null, (Effect[]) null);
    }

    /**
     * Creates a new {@link ComponentPainter}
     * with the given <code>component</code>
     * 
     * @param component the component to be rendered by this painter
     */
    public ComponentPainter(JComponent component) {
        this(component, (Effect[]) null);
    }

    /**
     * Creates a new {@link ComponentPainter}
     * with the given <code>component</code>
     * and collection of the {@link Effect}s
     * 
     * @param component the component to be rendered by this painter
     * @param effects the collection of the {@link Effect}s 
     * to be applied to the buffer of this painter
     */
    public ComponentPainter(JComponent component, Effect... effects) {
        setComponent(component);
        setEffects(effects);
    }

    /**
     * Gets the component to be rendered by this painter
     *  
     * @return the component to be rendered by this painter
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * Sets the component to be rendered by this painter;
     * <p/>
     * Note: call {@link #update()} to take the actual screenshot of the component
     *  
     * @param component the component to be rendered by this painter
     */
    public void setComponent(JComponent component) {
        this.component = component;
        fireLayerItemChanged();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isPainterValid() {
        return component != null &&
                component.getWidth() != 0 && component.getHeight() != 0;
    }

    /**
     * Takes a screenshot of the component returned by the
     * {@link #getComponent()} method and set it with {@link #setImage(Image)}
     * and calls {@link #validate()} which notifies all painter's {@link LayerItemListener}s
     *
     * @see #getComponent()
     * @see #validate()
     * @see #fireLayerItemChanged() 
     */
    public void update() {
        if (isPainterValid()) {
            Image image = getImage();
            if (image == null ||
                    image.getWidth(null) != component.getWidth() ||
                    image.getHeight(null) != component.getHeight()) {
                image = createBuffer(component.getWidth(), component.getHeight());
                setImage(image);
            }
            Graphics g = image.getGraphics();
            component.paint(g);
            g.dispose();
        }
        validate();
    }
}
