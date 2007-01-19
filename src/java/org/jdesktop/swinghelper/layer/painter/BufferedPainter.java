package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.effect.Effect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedPainter<V extends JComponent>
        extends CompoundPainter<V> {

    private BufferedImage buffer;
    private Effect[] effects = new Effect[0];

    public BufferedPainter() {
        this((Painter[]) null);
    }

    public BufferedPainter(Painter<V>... painters) {
        super(painters);
        effects = new Effect[0];
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
        if (buffer == null ||
                buffer.getWidth() != l.getWidth() ||
                buffer.getHeight() != l.getHeight()) {
            buffer = createBuffer(g2, l.getWidth(), l.getHeight());
        }
        Graphics2D bufg = (Graphics2D) buffer.getGraphics();
        processPainters(bufg, l);
        for (Effect<V> e : effects) {
            if (e.isEnabled()) {
                buffer = e.apply(buffer, l);
            }
        }
        g2.drawImage(buffer, 0, 0, null);
        bufg.dispose();
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    protected BufferedImage createBuffer(Graphics2D g2, int width, int height) {
        return g2.getDeviceConfiguration().
                createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public void setEffects(Effect<V>... effects) {
        if (effects == null) {
            effects = new Effect[0];
        }
        for (Effect<V> effect : getEffects()) {
            effect.removeChangeListener(getHandler());
        }
        this.effects = new Effect[effects.length];
        System.arraycopy(effects, 0, this.effects, 0, effects.length);
        for (Effect<V> effect : effects) {
            effect.addChangeListener(getHandler());
        }
        fireStateChanged();
    }

    public Effect<V>[] getEffects() {
        Effect<V>[] result = new Effect[effects.length];
        System.arraycopy(effects, 0, result, 0, result.length);
        return result;
    }
}
