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
import org.jdesktop.swinghelper.layer.effectlist.EffectList;

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
public class JXLayer extends JPanel {
    // layering 
    private JComponent contentPane;
    private JComponent glassPane;

    // painting 
    private boolean isAdvancedPaintingEnabled;
    private BufferedImageOp bufferedImageOp;
    private float alpha;
    private Painter backgroundPainter;
    private boolean isBackgroundPainterOrigin;
    private Painter foregroundPainter;
    private boolean isForegroundPainterOrigin;
    private Shaper shaper;
    private boolean isUseEffectList;
    private EffectList effectList;

    // temp
    private BufferedImage tempSrc;
    private BufferedImage tempDst;

    public JXLayer() {
        this(true);
    }

    public JXLayer(boolean isAdvancedPaintingEnabled) {
        this(new FlowLayout(), isAdvancedPaintingEnabled);
    }

    public JXLayer(JComponent c) {
        this(c, true);
    }

    public JXLayer(JComponent c, boolean isAdvancedPaintingEnabled) {
        this((LayoutManager) null, isAdvancedPaintingEnabled);
        setContentPane(c);
    }

    public JXLayer(LayoutManager layout) {
        this(layout, true);
    }

    public JXLayer(LayoutManager layout, boolean isAdvancedPaintingEnabled) {
        super(null);
        setContentPane(new JPanel());
        setGlassPane(new JXGlassPane());
        getContentPane().setLayout(layout);

        setAdvancedPaintingEnabled(isAdvancedPaintingEnabled);
        setAlpha(1f);
        setBackgroundPainterOrigin(true);
        setForegroundPainterOrigin(true);
        setUseEffectList(true);
    }

    public void doLayout() {
        if (contentPane != null) {
            setPreferredSize(contentPane.getPreferredSize());
            contentPane.setLocation(0, 0);
            contentPane.setSize(getWidth(), getHeight());
        }
        if (glassPane != null) {
            glassPane.setLocation(0, 0);
            glassPane.setSize(getWidth(), getHeight());
        }
    }

    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        contentPane.add(comp, constraints, index);
        doLayout();
    }

    public void remove(Component comp) {
        contentPane.remove(comp);
    }

    public void removeAll() {
        contentPane.removeAll();
    }

    public void setLayout(LayoutManager mgr) {
        if (contentPane != null) {
            contentPane.setLayout(mgr);
        }
    }

    public LayoutManager getLayout() {
        return contentPane.getLayout();
    }

    public JComponent getContentPane() {
        return contentPane;
    }

    public void setContentPane(JComponent contentPane) {
        if (contentPane == null) {
            throw new IllegalArgumentException("ContentPane can't be set to null");
        }
        if (getContentPane() != null) {
            super.remove(contentPane);
        }
        super.addImpl(contentPane, null, getComponentCount());
        this.contentPane = contentPane;
    }

    public JComponent getGlassPane() {
        return glassPane;
    }

    public void setGlassPane(JComponent glassPane) {
        if (glassPane == null) {
            throw new IllegalArgumentException("GlassPane can't be set to null");
        }
        if (getGlassPane() != null) {
            super.remove(glassPane);
        }
        super.addImpl(glassPane, null, 0);
        this.glassPane = glassPane;
    }

    public void setPreferredSize(Dimension preferredSize) {
        contentPane.setPreferredSize(preferredSize);
    }

    public Dimension getPreferredSize() {
        return contentPane.getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return contentPane.getMaximumSize();
    }

    public void setMaximumSize(Dimension maximumSize) {
        contentPane.setMaximumSize(maximumSize);
    }

    public Dimension getMinimumSize() {
        return contentPane.getMinimumSize();
    }

    public void setMinimumSize(Dimension minimumSize) {
        contentPane.setMinimumSize(minimumSize);
    }

    // painting

    public boolean isAdvancedPaintingEnabled() {
        return isAdvancedPaintingEnabled;
    }

    public void setAdvancedPaintingEnabled(boolean isAdvancedPaintingEnabled) {
        if (isAdvancedPaintingEnabled() != isAdvancedPaintingEnabled) {
            this.isAdvancedPaintingEnabled = isAdvancedPaintingEnabled;
            repaint();
        }
    }

    public BufferedImageOp getBufferedImageOp() {
        if (isUseEffectList && effectList != null) {
            return effectList.getBufferedImageOp(bufferedImageOp, this);
        }
        return bufferedImageOp;
    }

    public void setBufferedImageOp(BufferedImageOp bio) {
        if (bio instanceof AffineTransformOp) {
            throw new IllegalArgumentException("AffineTransformOp is not supported");
        }
        this.bufferedImageOp = bio;
        repaint();
    }

    public float getAlpha() {
        if (isUseEffectList && effectList != null) {
            return effectList.getAlpha(alpha, this);
        }
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
        if (isUseEffectList && effectList != null) {
            return effectList.getBackgroundPainter(backgroundPainter, this);
        }
        return backgroundPainter;
    }

    public void setBackgroundPainter(Painter backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
        repaint();
    }

    public boolean isBackgroundPainterOrigin() {
        if (isUseEffectList && effectList != null) {
            return effectList.isBackgroundPainterOrigin(isBackgroundPainterOrigin, this);
        }
        return isBackgroundPainterOrigin;
    }

    public void setBackgroundPainterOrigin(boolean backgroundPainterOrigin) {
        isBackgroundPainterOrigin = backgroundPainterOrigin;
        repaint();
    }

    public Painter getForegroundPainter() {
        if (isUseEffectList && effectList != null) {
            return effectList.getForegroundPainter(foregroundPainter, this);
        }
        return foregroundPainter;
    }

    public void setForegroundPainter(Painter foregroundPainter) {
        this.foregroundPainter = foregroundPainter;
        repaint();
    }

    public boolean isForegroundPainterOrigin() {
        if (isUseEffectList && effectList != null) {
            return effectList.isForegroundPainterOrigin(isForegroundPainterOrigin, this);
        }
        return isForegroundPainterOrigin;
    }

    public void setForegroundPainterOrigin(boolean foregroundPainterOrigin) {
        isForegroundPainterOrigin = foregroundPainterOrigin;
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

        BufferedImageOp bufferedImageOp = getBufferedImageOp();
        float alpha = getAlpha();
        Painter backgroundPainter = getBackgroundPainter();
        boolean isBackgroundPainterOrigin = isBackgroundPainterOrigin();
        Painter foregroundPainter = getForegroundPainter();
        boolean isForegroundPainterOrigin = isForegroundPainterOrigin();
        Shaper shaper = getShaper();

        Shape clip = g2.getClip();
        if (shaper != null) {
            Shape shape = getLayerShape();
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

        boolean isConvolveOp = bufferedImageOp instanceof ConvolveOp;

        if (isConvolveOp) {
            ConvolveOp cop = (ConvolveOp) bufferedImageOp;
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

        if (backgroundPainter != null && !isBackgroundPainterOrigin) {
            Graphics2D temp = (Graphics2D) bufg.create();
            backgroundPainter.paint(temp, this);
            temp.dispose();
        }

        super.paint(bufg);

        if (foregroundPainter != null && !isForegroundPainterOrigin) {
            Graphics2D temp = (Graphics2D) bufg.create();
            foregroundPainter.paint(temp, this);
            temp.dispose();
        }

        if (bufferedImageOp != null) {
            if (isConvolveOp) {
                tempDst = bufferedImageOp.filter(tempSrc, tempDst);
            } else {
                tempDst = bufferedImageOp.filter(tempSrc, tempSrc);
            }
        } else {
            tempDst = tempSrc;
        }

        if (isOpaque()) {
            g2.clearRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
        }

        if (backgroundPainter != null && isBackgroundPainterOrigin) {
            Graphics2D temp = (Graphics2D) g2.create();
            backgroundPainter.paint(temp, this);
            temp.dispose();
        }

        Composite oldComposite = g2.getComposite();
        if (alpha != 1) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        g2.drawImage(tempDst, clipBounds.x, clipBounds.y, null);
        g2.setComposite(oldComposite);

        if (foregroundPainter != null && isForegroundPainterOrigin) {
            Graphics2D temp = (Graphics2D) g2.create();
            foregroundPainter.paint(temp, this);
            temp.dispose();
        }
        g2.dispose();
    }

    // Shaping

    public Shaper getShaper() {
        if (isUseEffectList && effectList != null) {
            return effectList.getShaper(shaper, this);
        }
        return shaper;
    }

    /**
     * Never returns <code>null</code>
     */
    public Shape getLayerShape() {
        Shaper shaper = getShaper();
        if (shaper != null) {
            Shape shape = shaper.getLayerShape(this);
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
        if (getShaper() != null) {
            return getLayerShape().contains(x, y);
        }
        return super.contains(x, y);
    }

    // EffectList

    public boolean isUseEffectList() {
        return isUseEffectList;
    }

    public void setUseEffectList(boolean useEffectList) {
        isUseEffectList = useEffectList;
        repaint();
    }

    public EffectList getEffectList() {
        return effectList;
    }

    public void setEffectList(EffectList effectList) {
        this.effectList = effectList;
        repaint();
    }
}
