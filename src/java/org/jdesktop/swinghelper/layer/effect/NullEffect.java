package org.jdesktop.swinghelper.layer.effect;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.image.BufferedImage;

final public class NullEffect<V extends JComponent>
        extends Effect<V> {
    public BufferedImage apply(BufferedImage buf, JXLayer<V> l) {
        return buf;
    }
}
