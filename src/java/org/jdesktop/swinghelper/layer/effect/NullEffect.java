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

package org.jdesktop.swinghelper.layer.effect;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;

final public class NullEffect <V extends JComponent>
        implements Effect<V> {

    public BufferedImage apply(BufferedImage buf, JXLayer<V> l) {
        return buf;
    }

    public boolean isEnabled() {
        return false;
    }

    public void setEnabled(boolean b) {
    }

    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }

    public ChangeListener[] getChangeListeners() {
        return new ChangeListener[0];
    }
}
