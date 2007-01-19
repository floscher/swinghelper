package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

public class DefaultPainter <V extends JComponent>
        extends Painter<V> {

    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
        l.paint(g2);
    }
}
