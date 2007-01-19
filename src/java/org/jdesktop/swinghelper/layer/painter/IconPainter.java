package org.jdesktop.swinghelper.layer.painter;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

public class IconPainter <V extends JComponent>
        extends Painter<V> {

    private Icon icon;

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void paint(Graphics2D g2, JXLayer<V> l) {
        configure(g2, l);
        if (icon != null) {
            icon.paintIcon(l, g2, 0, 0);
        }
    }
}
