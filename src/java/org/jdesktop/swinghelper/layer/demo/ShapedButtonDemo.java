package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.shaper.Shaper;
import org.jdesktop.swinghelper.layer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class ShapedButtonDemo {

    private static void createGui() {
        JFrame frame = new JFrame("ShapedButtonDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Since we use shaping we need button's parent 
        // to be repainted toghether with the button, 
        // so to make it we set JXLayer as the contentPane;
        // the <code>false</code> as the paramter switches off 
        // advanced painting (painters, alpha etc...) for this JXLayer
        // and it is used just as an extra "GlassPane"
        frame.setContentPane(new JXLayer(false));
        
        JButton button = new JButton("Hello");
        button.setPreferredSize(new Dimension(150, 100));
        
        // Wrap the button with JXLayer
        JXLayer layer = new JXLayer(button);
        frame.add(layer);
        
        // Ellipse shape for the layer
        layer.setShaper(new Shaper() {
            public Shape getShape(JXLayer l) {
                return new Ellipse2D.Float(0, 0, l.getWidth(), l.getHeight());
            }
        });
        
        // default rectangle border doesn't go
        button.setBorderPainted(false);

        layer.setForegroundPainter(new Painter() {
            public void paint(Graphics2D g2, JXLayer l) {
                ButtonModel model = ((JButton) l.getContentPane()).getModel();

                // new rollover effect (just for fun)
                if (model.isRollover() || model.isArmed()) {
                    g2.setColor(Color.GREEN);
                    Composite oldComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
                    g2.fillRect(0, 0, l.getWidth(), l.getHeight());
                    g2.setComposite(oldComposite);
                }

                // paint shaped border
                g2.setColor(new Color(184, 207, 229));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(l.getShape());
            }
        });

        frame.setSize(250, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGui();
            }
        });
    }
}

