package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class DisabledLayerDemo {
    private static JXLayer<JComponent> layer; 
    
    public static void main(String[] args) throws Exception {
        final JFrame frame = new JFrame("Disabled/enabled layer demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layer = new JXLayer<JComponent>(createLayerPanel());
        
        layer.setPainter(new DefaultPainter<JComponent>() {
            public void paint(Graphics2D g2, JXLayer<JComponent> l) {
                super.paint(g2, l);
                if (!l.isEnabled()) {
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(5f));
                    g2.drawLine(0, 0, l.getWidth(), l.getHeight());
                }
            }
        });
        
        frame.add(layer);

        frame.add(createToolPanel(), BorderLayout.EAST);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComponent createLayerPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JButton("JButton"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField("JTextField"));
        panel.add(new JSlider(0, 100));
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }
    
    private static JComponent createToolPanel() {
        JComponent box = Box.createVerticalBox();
        JCheckBox checkbox = new JCheckBox("Press me !");
        checkbox.setFocusable(false);
        checkbox.setToolTipText("Unfocusable checkbox");
        checkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                layer.setEnabled(e.getStateChange() == ItemEvent.DESELECTED);
            }
        });
        box.add(Box.createGlue());
        box.add(checkbox);
        box.add(Box.createGlue());
        box.add(new JButton("Focusable button"));
        box.add(Box.createGlue());
        return box;
    }       
}
