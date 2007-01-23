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

package org.jdesktop.swinghelper.layer.shaper;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;
import org.jdesktop.swinghelper.layer.painter.configurator.Configurator;

import javax.swing.*;
import java.awt.*;

public class DefaultShaper <V extends JComponent>
        extends AbstractShaper<V> {

    public boolean contains(int x, int y, JXLayer<V> l) {
        Shape clip = null;
        if (l.getPainter().isEnabled()) {
            PainterModel model = l.getPainter().getModel();
            if (model.isEnabled()) {
                clip = model.getClip();
            }
            Configurator<V> configurator = l.getPainter().getConfigurator();
            if (configurator.isEnabled() && configurator.getClip(l) != null) {
                clip = configurator.getClip(l);
            }
        }
        return clip == null ? true : clip.contains(x, y);
    }
}
