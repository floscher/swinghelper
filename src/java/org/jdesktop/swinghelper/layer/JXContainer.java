/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
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

import org.jdesktop.swinghelper.layer.painter.Painter;
import org.jdesktop.swinghelper.layer.shaper.Shaper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.*;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class JXContainer extends JPanel {
    private BufferedImageOp bio;
    private BufferedImage tempSrc;
    private BufferedImage tempDst;
    private float alpha;
    private Painter backgroundPainter;
    private Painter foregroundPainter;
    private Shaper shaper;
    private boolean isAdvancedPaintingEnabled; 

    public JXContainer() {
        this(new FlowLayout());
    }

    public JXContainer(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        alpha = 1f;
        isAdvancedPaintingEnabled = true;
    }

    public boolean isAdvancedPaintingEnabled() {
        return isAdvancedPaintingEnabled;
    }

    public void setAdvancedPaintingEnabled(boolean advancedPaintingEnabled) {
        if (isAdvancedPaintingEnabled != advancedPaintingEnabled) {
            isAdvancedPaintingEnabled = advancedPaintingEnabled;
            repaint();
        }
    }

    public BufferedImageOp getBufferedImageOp() {
        return bio;
    }

    public void setBufferedImageOp(BufferedImageOp bio) {
        if (bio instanceof AffineTransformOp) {
            throw new IllegalArgumentException("AffineTransformOp is not supported");
        }
        this.bio = bio;
        repaint();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException();
        }
        this.alpha = alpha;
        repaint();
    }

    public Painter getBackgroundPainter() {
        return backgroundPainter;
    }

    public void setBackgroundPainter(Painter bacgroundPainter) {
        this.backgroundPainter = bacgroundPainter;
        repaint();
    }

    public Painter getForegroundPainter() {
        return foregroundPainter;
    }

    public void setForegroundPainter(Painter foregroundPainter) {
        this.foregroundPainter = foregroundPainter;
        repaint();
    }

    public void paint(Graphics g) {
        if (!isAdvancedPaintingEnabled()) {
            super.paint(g);
            return;
        }

        if (!(g instanceof Graphics2D)) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        Shape clip = g2.getClip();
        if (shaper != null) {
            Shape shape = getShape();
            if (clip == null) {
                g2.setClip(shape);
            } else {
                Area area = new Area(clip);
                area.intersect(new Area(shape));
                g2.setClip(area);
            }
        }

        Rectangle clipBounds = g2.getClipBounds();

        if (clipBounds == null) {
            clipBounds = new Rectangle(getSize());
        }
        if (clipBounds.isEmpty()) {
            return;
        }

        boolean isConvolveOp = bio instanceof ConvolveOp;

        if (isConvolveOp) {
            ConvolveOp cop = (ConvolveOp) bio;
            Kernel kernel = cop.getKernel();
            clipBounds.grow(kernel.getWidth() / 2, kernel.getHeight() / 2);
        }

        if (tempSrc == null ||
                tempSrc.getWidth() != clipBounds.width || tempSrc.getHeight() != clipBounds.height) {
            tempSrc = getGraphicsConfiguration().createCompatibleImage(clipBounds.width, clipBounds.height);
            if (isConvolveOp) {
                tempDst = getGraphicsConfiguration().createCompatibleImage(clipBounds.width, clipBounds.height);
            }
        }

        Graphics2D bufg = (Graphics2D) tempSrc.getGraphics();

        bufg.translate(-clipBounds.x, -clipBounds.y);

        bufg.setClip(clipBounds);

        super.paint(bufg);

        if (bio != null) {
            if (isConvolveOp) {
                tempDst = bio.filter(tempSrc, tempDst);
            } else {
                tempDst = bio.filter(tempSrc, tempSrc);
            }
        } else {
            tempDst = tempSrc;
        }

        if (alpha != 1) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }

        if (backgroundPainter != null) {
            Graphics2D temp = (Graphics2D) g2.create();
            temp.clearRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
            backgroundPainter.paint(temp, getPaintingDelegate());
            temp.dispose();
        }

        g2.drawImage(tempDst, clipBounds.x, clipBounds.y, null);

        if (foregroundPainter != null) {
            Graphics2D temp = (Graphics2D) g2.create();
            foregroundPainter.paint(temp, getPaintingDelegate());
            temp.dispose();
        }
        g2.dispose();
    }

    public Shaper getShaper() {
        return shaper;
    }

    public Shape getShape() {
        if (shaper != null) {
            Shape shape = shaper.getShape(getPaintingDelegate());
            if (shape != null) {
                return shape;
            }
        }
        return new Rectangle(getSize());
    }

    public void setShaper(Shaper shaper) {
        this.shaper = shaper;
        repaint();
    }

    public boolean contains(int x, int y) {
        if (shaper != null) {
            return getShape().contains(x, y);
        }
        return super.contains(x, y);
    }
    
    protected Component getPaintingDelegate() {
        return this;
    }
}
