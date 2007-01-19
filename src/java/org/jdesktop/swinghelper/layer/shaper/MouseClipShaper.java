package org.jdesktop.swinghelper.layer.shaper;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

public class MouseClipShaper<V extends JComponent> extends Shaper<V> {
    public Shape getShape(JXLayer<V> l) {
        return l.getMouseClipShaper().getShape(l);
    }
}
