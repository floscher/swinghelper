package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

final public class NullPainter<V extends JComponent> extends Painter<V> {
    public void paint(Graphics2D g2, JXLayer<V> l) {
    }
}
