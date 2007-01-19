package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ComponentPainter<V extends JComponent>
        extends Painter<V> {

    private JComponent component;
    private BufferedImage buf;
    private Painter<V> delegatee;

    public ComponentPainter() {
        this(null);
    }

    public ComponentPainter(JComponent component) {
        setComponent(component);
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
        fireStateChanged();
    }

    public void repaint() {
        if (component != null &&
                component.getWidth() != 0 && component.getHeight() != 0) {
            if (buf == null ||
                    buf.getWidth() != component.getWidth() ||
                    buf.getHeight() != component.getHeight()) {
                buf = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            }
            Graphics g = buf.getGraphics();
            component.paint(g);
            g.dispose();
            fireStateChanged();
        }
    }

    public BufferedImage getBuffer() {
        return buf;
    }

    public Painter<V> getDelegatee() {
        return delegatee;
    }

    public void setDelegatee(Painter<V> delegatee) {
        this.delegatee = delegatee;
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
        g2.drawImage(getBuffer(), 0, 0, null);
    }
}
