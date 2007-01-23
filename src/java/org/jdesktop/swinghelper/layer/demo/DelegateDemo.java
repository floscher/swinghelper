package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class DelegateDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("DelegateDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLayout(new GridBagLayout());
        JButton button = new JButton("Play with me !");
        button.setToolTipText("I am an unmodified JButton");
        
        Painter<AbstractButton> customPainter = new DefaultPainter<AbstractButton>() {
            public void paint(Graphics2D g2, JXLayer<AbstractButton> l) {
                super.paint(g2, l);
                
                final ButtonModel model = l.getView().getModel();
                Color color = null;
                if (model.isRollover()) {
                    color = Color.GREEN;
                }
                if (model.isPressed()) {
                    color = Color.RED;
                }
                if (model.isArmed()) {
                    color = Color.YELLOW;
                }
                if (color != null) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
                    g2.setColor(color);
                    g2.fillRect(0, 0, l.getWidth(), l.getHeight());
                }
            }
        };
        
        JXLayer<AbstractButton> l = new JXLayer<AbstractButton>(button, customPainter);
        frame.add(l);

        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
