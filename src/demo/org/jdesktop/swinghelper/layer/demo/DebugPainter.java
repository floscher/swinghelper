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

package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.painter.BufferedPainter;
import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class DebugPainter<V extends JComponent> extends BufferedPainter<V> implements ActionListener {

    private Map<Shape, DebugPainterEntry> shapeMap = new HashMap<Shape, DebugPainterEntry>();
    private boolean skipRepaint;
    private final Timer timer;

    public DebugPainter() {
        this(50);
    }

    public DebugPainter(int delay) {
        timer = new Timer(delay, this);
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {

        // After this line the painter's buffer is up-to-date;
        // I use getBuffer() below
        super.paint(g2, l);

        Shape clip = g2.getClip();
        if (clip != null && !shapeMap.containsKey(clip) &&
                !(skipRepaint && clip.equals(l.getVisibleRect()))) {
            Rectangle bounds = clip.getBounds();

            // create inverted image for the current clip
            BufferedImage buf = new BufferedImage(bounds.width, bounds.height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics bufg = buf.getGraphics();
            bufg.setColor(Color.WHITE);
            bufg.fillRect(0, 0, buf.getWidth(), buf.getHeight());
            bufg.setXORMode(Color.BLACK);
            bufg.drawImage(getBuffer(), 0, 0, bounds.width, bounds.height,
                    bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, null);
            bufg.dispose();
            shapeMap.put(clip, new DebugPainterEntry(buf, bounds.x, bounds.y));
            if (!timer.isRunning()) {
                timer.start();
            }
        }

        // Painting clip shapes
        Set<Shape> shapes = shapeMap.keySet();
        for (Iterator<Shape> iterator = shapes.iterator(); iterator.hasNext();) {
            Shape shape = iterator.next();
            DebugPainterEntry entry = shapeMap.get(shape);
            if (entry.alpha <= 0) {
                iterator.remove();
                continue;
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, entry.alpha));
            g2.drawImage(entry.image, entry.x, entry.y, null);
            entry.alpha -= .1;
        }
        skipRepaint = false;
    }

    public void actionPerformed(ActionEvent e) {
        if (!shapeMap.isEmpty()) {
            skipRepaint = true;
            fireLayerItemChanged();
        } else {
            timer.stop();
        }
    }

    public void setDelay(int delay) {
        timer.setDelay(delay);
    }

    private static class DebugPainterEntry {
        private Image image;
        private int x;
        private int y;
        private float alpha = 1;

        public DebugPainterEntry(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }
    }
}
