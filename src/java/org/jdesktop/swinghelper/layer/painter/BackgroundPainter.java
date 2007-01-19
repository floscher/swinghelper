package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

public class BackgroundPainter <V extends JComponent> 
        extends Painter<V> {
    
    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
        V view = l.getView();
        if (view == null) {
            g2.setColor(l.getBackground());
        } else {
            g2.setColor(view.getBackground());
        }
        g2.fillRect(0, 0, l.getWidth(), l.getHeight());
    }
}
