package org.jdesktop.swinghelper.draft;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.*;

/**
 * This is a slight modification of the example from 
 * <a href="http://www.javalobby.org/java/forums/t17808.html">Swing: Debug your UI by yourself</a>  
 * by Romain Guy
 */

public class DebugEventListener implements AWTEventListener {
    public void eventDispatched(AWTEvent event) {
        Object o = event.getSource();
        if (o instanceof JComponent) {
            JComponent source = (JComponent) o;
            Border border = source.getBorder();

            switch (event.getID()) {
                case MouseEvent.MOUSE_ENTERED:
                    source.setBorder(new DebugBorder(border));
                    break;
                case MouseEvent.MOUSE_EXITED:
                    source.setBorder(((DebugBorder) border).getDelegate());
                    break;
            }
        }
    }

    public class DebugBorder implements Border {
        private Border b;

        public DebugBorder(Border b) {
            this.b = b;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color layerColor = new Color(0.0f, 1.0f, 0.0f, 0.35f);
            g.setColor(layerColor);
            g.fillRect(x, y, width, height);
            if (b != null) {
                b.paintBorder(c, g, x, y, width, height);
            }
        }

        public Insets getBorderInsets(Component c) {
            return b != null ? b.getBorderInsets(c):new Insets(0,0,0,0); 
        }

        public boolean isBorderOpaque() {
            return b != null ? b.isBorderOpaque(): false;
        }

        public Border getDelegate() {
            return b;
        }
    }


    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                Toolkit.getDefaultToolkit().addAWTEventListener(new DebugEventListener(), AWTEvent.MOUSE_EVENT_MASK);

                final JFrame frame = new JFrame("DebugEventListener demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new FlowLayout());

                frame.add(new JButton("Hello"));
                frame.add(new JSlider());
                
                JLabel label = new JLabel("JLabel with no MouseListeners");
                //Component with no any Mouse/MouseMotionListeners attached don't get mouseEvents !
                //if you want to color them too catch MouseMotion events with AWTEventListener
                //and use SwingUtilities.getDeepestComponentAt() to find the component under cursor
                
                //uncomment this line, and JLabel will start to get MouseEvents 
                //label.addMouseListener(new MouseAdapter() {});
                
                frame.add(label);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
