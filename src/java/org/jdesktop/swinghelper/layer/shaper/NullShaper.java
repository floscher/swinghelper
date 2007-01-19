package org.jdesktop.swinghelper.layer.shaper;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

final public class NullShaper<V extends JComponent> 
        extends Shaper<V> {
    public Shape getShape(JXLayer<V> l) {
        return null;
    }
}
