package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXGlassPane;
import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.CompoundPainter;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;
import org.jdesktop.swinghelper.layer.shaper.MouseClipShaper;
import org.jdesktop.swinghelper.layer.shaper.Shaper;

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
        // to be repainted toghether with the button;
        // making the glassPane visible is the simplest way to do it 
        frame.setGlassPane(new JXGlassPane());
        frame.getGlassPane().setVisible(true);
        
        JButton button = new JButton("Hello");
        button.setPreferredSize(new Dimension(150, 100));
        
        // Wrap the button with JXLayer
        JXLayer<AbstractButton> layer = new JXLayer<AbstractButton>(button);
        frame.add(layer);
        
        // Ellipse shape for the layer
        layer.setMouseClipShaper(new Shaper() {
            public Shape getShape(JXLayer l) {                
                return new Ellipse2D.Float(0, 0, l.getWidth(), l.getHeight());
            }
        });
        
        // default rectangle border doesn't go
        button.setBorderPainted(false);
        button.setBackground(Color.GREEN.darker());

        Painter<AbstractButton> customPainter = new Painter<AbstractButton>() {
            public void paint(Graphics2D g2, JXLayer<AbstractButton> l) {

                ButtonModel model = l.getView().getModel();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // new rollover effect (just for fun)
                if (model.isRollover() || model.isArmed()) {
                    g2.setColor(Color.GREEN.brighter());
                    Composite oldComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
                    g2.fillRect(0, 0, l.getWidth(), l.getHeight());
                    g2.setComposite(oldComposite);
                }

                // paint shaped border
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3f));
                g2.draw(l.getMouseClipShaper().getShape(l));               
            }
        };
        
        layer.setPainter(
                new CompoundPainter<AbstractButton>(
                        new DefaultPainter(), customPainter));
        
        layer.getPainter().setClipShaper(new MouseClipShaper<AbstractButton>());
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

