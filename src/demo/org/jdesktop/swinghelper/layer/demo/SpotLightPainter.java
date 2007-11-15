package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.AbstractBufferedPainter;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * SpotLightPainter paints the layer as is and then 
 * it paints translucent mask on the top.
 * <p>
 * Here you can see the example of overridden <code>contains()</code> method,
 * which implements a custom mouseEvent filter;<br>
 * A mouseEvent will be processed only if any of added shapes contain its coordinates
 * <p>
 * The mask is generated with help of the buffered <code>foregroundPainter</code>;<br>
 * The perfomance if very good because most of the time the cached image is used, 
 * and it is repainted only when the whole layer is repainted,<br>
 * (e.g. when the frame is resized or when <code>fireLayerItemChanged</code> is called)
 * 
 * @see #paint(Graphics2D, JXLayer)
 * @see #contains(int, int, JXLayer)
 * @see AbstractBufferedPainter#isIncrementalUpdate(JXLayer)
 * 
 * @author Alexander Potochkin
 */
public class SpotLightPainter<V extends JComponent> 
        extends DefaultPainter<V> {

    /**
     * Clip list for this painter.
     */
    private ArrayList<Shape> clipList = new ArrayList<Shape>();

    /**
     * Overlay color for the non-matching items.
     */
    private Color overlayColor;

    /**
     * Foreground painter.
     */
    private Painter<V> foregroundPainter;

    private int softClipWidth;

    /**
     * Creates a simple spotlight painter.
     */
    public SpotLightPainter() {
        this(0);
    }
    
    public SpotLightPainter(int softClipWidth) {
        this(new Color(0, 0, 0, 128), softClipWidth);
    }
    
    public SpotLightPainter(final Color overlayColor, int softClipWidth) {
        setOverlayColor(overlayColor);
        setSoftClipWidth(softClipWidth);

        // AbstractBufferedPainter.getModel().isIncrementalUpdate() is false by default
        // so this painter will cache its buffer and repaint itself only
        // when the whole layer is repainted
        this.foregroundPainter = new AbstractBufferedPainter<V>() {
            @Override
            protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0, 0, l.getWidth(), l.getHeight());
                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(overlayColor);
                g2.fillRect(0, 0, l.getWidth(), l.getHeight());
                for (Shape shape : clipList) {
                    g2.setClip(shape);
                    g2.setComposite(AlphaComposite.Clear);
                    g2.fill(shape);
                    softClipping(g2, shape);
                }
            }
        };
    }

    private void softClipping(Graphics2D g2, Shape shape) {
        g2.setComposite(AlphaComposite.Src);
        for (int i = 0; i < softClipWidth; i++) {
            int alpha = (i + 1) * overlayColor.getAlpha()
                    / (softClipWidth + 1);
            Color temp = new Color(
                    overlayColor.getRed(),
                    overlayColor.getGreen(), 
                    overlayColor.getBlue(), alpha);
            g2.setColor(temp);
            g2.setStroke(new BasicStroke(softClipWidth - i));
            g2.draw(shape);
        }
    }

    public Color getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(Color overlayColor) {
        if (overlayColor == null) {
            throw new IllegalArgumentException("overlayColor is null");
        }
        this.overlayColor = overlayColor;
        fireLayerItemChanged();
    }

    public int getSoftClipWidth() {
        return softClipWidth;
    }

    public void setSoftClipWidth(int softClipWidth) {
        if (softClipWidth < 0) {
            throw new IllegalArgumentException("softClipWidth can't be less than 0");
        }
        this.softClipWidth = softClipWidth;
        fireLayerItemChanged();
    }

    /**
     * Resets this painter.
     */
    public void reset() {
        clipList.clear();
        fireLayerItemChanged();
    }

    /**
     * Adds the specified shape to the slip list.
     *
     * @param shape Shape to add to the clip list.
     */
    public void addShape(Shape shape) {
        clipList.add(shape);
    }

    @Override
    public void paint(Graphics2D g2, JXLayer<V> l) {
        super.paint(g2, l);
        foregroundPainter.paint(g2, l);
    }

    @Override
    public boolean contains(int x, int y, JXLayer<V> l) {
        for (Shape shape : clipList) {
            if (shape.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
}

