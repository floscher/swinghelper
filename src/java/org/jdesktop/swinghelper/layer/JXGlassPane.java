package org.jdesktop.swinghelper.layer;

import javax.swing.*;

public class JXGlassPane extends JPanel {
    public JXGlassPane() {
        setOpaque(false);
    }

    public boolean contains(int x, int y) {
        if (getMouseListeners().length == 0
                && getMouseMotionListeners().length == 0
                && getMouseWheelListeners().length ==0
                && !isCursorSet()) {
            return false;
        }
        return super.contains(x, y);
    }
}
