package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SpotLightDemo extends JFrame {
    private SpotLightPainter<JComponent> painter = 
            new SpotLightPainter<JComponent>();

    private Ellipse2D shape;
    
    public SpotLightDemo() {
        super("SpotLight effect demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JXLayer<JComponent> layer = 
                new JXLayer<JComponent>(createDemoPanel(4, 5),
                        painter);
        JScrollPane pane = new JScrollPane(layer);
        add(pane);

        shape = new Ellipse2D.Double(20, 20, 120, 120);
        painter.addShape(shape);
        
        add(createToolPanel(), BorderLayout.SOUTH);
        
        setSize(400, 300);
        setLocationRelativeTo(null);
        setJMenuBar(LafMenu.createMenuBar());
    }

    private JComponent createDemoPanel(int w, int h) {
        JPanel panel = new JPanel(new GridLayout(w, h));
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                panel.add(new JButton("Hello"));
            }
        }
        panel.setPreferredSize(new Dimension(500, 400));
        return panel;
    }
    
    private JComponent createToolPanel() {
        JPanel panel = new JPanel();
        final JSpinner xspinner = new JSpinner(
                new SpinnerNumberModel((int) shape.getX(), 0, 400, 5));
        final JSpinner yspinner = new JSpinner(
                new SpinnerNumberModel((int) shape.getY(), 0, 400, 5));
        
        ChangeListener listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                painter.reset();
                shape.setFrame((Integer) xspinner.getValue(), 
                        ((Integer)yspinner.getValue()),
                        shape.getWidth(), shape.getHeight());
                painter.addShape(shape);
            }
        };
        xspinner.addChangeListener(listener);
        yspinner.addChangeListener(listener);
        
        panel.add(new JLabel("X:"));
        panel.add(xspinner);
        panel.add(new JLabel(" Y:"));
        panel.add(yspinner);
        
        final JSpinner clipSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        clipSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                painter.setSoftClipWidth((Integer) clipSpinner.getValue());
            }
        });

        panel.add(new JLabel(" Border size:"));
        panel.add(clipSpinner);
        return panel;
    }
    
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SpotLightDemo().setVisible(true);
            }
        });
    }
}
