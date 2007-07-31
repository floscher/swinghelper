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

import javax.swing.*;

/**
 * The default glassPane for the {@link JXLayer}.<br/>
 * It is the non-opaque panel with overridden {@link #contains(int, int)}
 * to enable custom cursors for inner components,<br/> 
 * for more details, see:
 * <p/>
 * <a href="http://weblogs.java.net/blog/alexfromsun/archive/2006/09/a_wellbehaved_g.html">A well-behaved GlassPane</a>
 */
public class JXGlassPane extends JPanel {
    /**
     * Creates a new {@link JXGlassPane}
     */
    public JXGlassPane() {
        setOpaque(false);
    }

    /**
     * If neither any mouseListeners is attached to this component
     * nor custom cursor is set, returns <code>false</false>,
     * otherwise calls parent implementation of this method 
     *  
     * @param x the <i>x</i> coordinate of the point
     * @param y the <i>y</i> coordinate of the point
     * @return true if this component logically contains x,y
     */
    public boolean contains(int x, int y) {
        if (getMouseListeners().length == 0
                && getMouseMotionListeners().length == 0
                && getMouseWheelListeners().length == 0
                && !isCursorSet()) {
            return false;
        }
        return super.contains(x, y);
    }
}
