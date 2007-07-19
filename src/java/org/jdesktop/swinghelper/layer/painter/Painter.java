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
import org.jdesktop.swinghelper.layer.item.LayerItem;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;

import javax.swing.*;
import java.awt.*;

/**
 * <p>A painting delegate for <code>JXLayer</code>,
 * <code>paint</code> method is used to control the visual appearance
 * and <code>contains</code> is used to filter our <code>MouseEvent</code>s
 * for <code>JXLayer</code>
 * <p><code>Painter</code> may be created with a type parameter
 * and this parameter must match with the type parameter
 * of <code>JXLayer</code> it is used for
 * <p/>
 * <pre><code>
 *      JButton button = new JButton("JButton");
 *      JXLayer<JButton> l = new JXLayer<JButton>(button);
 *      l.setPainter(new MyPainter<JButton>());
 * </code></pre>
 * <p/>
 * Note: <code>Painter</code> is designed to be shared 
 * among multiple <code>JXLayer</code>s
 *
 * @see JXLayer 
 * @see PainterModel
 * @see AbstractPainter
 */
public interface Painter<V extends JComponent> extends LayerItem {
    /**
     * <p>Renders the visual appearance of the given <code>JXLayer</code>
     * to the given {@link java.awt.Graphics2D} object.
     * The <code>g2</code> and <code>l</code>must never be null.</p>
     *
     * @param g2 The {@link java.awt.Graphics2D} to render to
     * @param l  The {@link JXLayer} to render for
     */
    public void paint(Graphics2D g2, JXLayer<V> l);

    /**
     * Returns the {@link PainterModel} for this painter
     *
     * @return the {@link PainterModel} for this painter
     */
    public PainterModel getModel();

    /**
     * Checks whether the given <code>JXLayer</code> accepts <code>MouseEvent</code>s
     * at the specified point or not, where <code>x</code> and <code>y</code> are defined to be
     * relative to the coordinate system of this component.
     *
     * @param x the <i>x</i> coordinate of the point
     * @param y the <i>y</i> coordinate of the point
     * @param l the {@link JXLayer} this point to be checked for
     * @return <code>true</code> if <code>MouseEvent</code>s are acceped in this point,
     *         <code>false</code> otherwise
     */
    public boolean contains(int x, int y, JXLayer<V> l);

    /**
     * May be used to read data to be used during painting from external sources 
     * or perform a lengthy task to set up this <code>Painter</code> 
     */
    public void update();
}
 