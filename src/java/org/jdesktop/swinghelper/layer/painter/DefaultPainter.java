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

import javax.swing.*;
import java.awt.*;

/**
 * The default implementation of the {@link AbstractPainter}
 * which paints the given {@link JXLayer} as is 
 */
public class DefaultPainter <V extends JComponent>
        extends AbstractPainter<V> {

    /**
     * Paints the given {@link JXLayer} as is  
     * <p/>
     * <strong>Note:</strong>You are free to change any state of the <code>g2</code> during painting,
     * there is no need to reset them at the end or create a defensive copy of <code>g2</code>;
     * {@link JXLayer} creates a defensive copy itself and passes it to its painters

     * @param g2 the {@link Graphics2D} instance to paint on
     * @param l  the {@link JXLayer} to paint for
     */
    public void paint(Graphics2D g2, JXLayer<V> l) {
        super.paint(g2, l);
        l.paint(g2);
    }
}
