package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

public class CompoundPainter<V extends JComponent>
        extends Painter<V> {

    private Painter[] painters = new Painter[0];

    public CompoundPainter() {
        this((Painter<V>[]) null);
    }

    public CompoundPainter(Painter<V>... painters) {
        setPainters(painters);
    }

    public Painter<V>[] getPainters() {
        Painter<V>[] result = new Painter[painters.length];
        System.arraycopy(painters, 0, result, 0, result.length);
        return result;
    }

    public void setPainters(Painter<V>... painters) {
        if (painters == null) {
            painters = new Painter[0];
        }
        for (Painter<V> painter : getPainters()) {
            painter.removeChangeListener(getHandler());
        }
        this.painters = new Painter[painters.length];
        System.arraycopy(painters, 0, this.painters, 0, painters.length);
        for (Painter<V> painter : painters) {
            painter.addChangeListener(getHandler());
        }
        fireStateChanged();
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
        processPainters(g2, l);
    }

    protected void processPainters(Graphics2D g2, JXLayer<V> l) {
        for (Painter<V> painter : painters) {
            Graphics2D temp = (Graphics2D) g2.create();
            if (painter.isEnabled()) {
                painter.paint(temp, l);
            }
            temp.dispose();
        }
    }

    public void repaint() {
        for (Painter<V> painter : painters) {
            painter.repaint();
        }
    }
}
