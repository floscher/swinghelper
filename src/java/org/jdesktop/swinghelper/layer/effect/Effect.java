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

package org.jdesktop.swinghelper.layer.effect;

import org.jdesktop.swinghelper.layer.item.LayerItem;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This interface describes an abstract operation 
 * performed on <code>BufferedImage</code>
 * 
 * @see ImageOpEffect
 */
public interface Effect extends LayerItem {
    /**
     * Applies the effect to the <code>BufferedImage</code>.
     * <p>The effect can take into account the passed <code>clip</code> shape
     * to speed the processing up
     *  
     * @param buf the <code>BufferedImage</code> to be processed 
     * @param clip the clip shape or <code>null</code> 
     * if the whole <code>BufferedImage</code> must be processed  
     */
    public void apply(BufferedImage buf, Shape clip);
}
