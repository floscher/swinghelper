package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.effect.Effect;
import org.jdesktop.swinghelper.layer.effect.ImageOpEffect;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.AbstractBufferedPainter;
import org.jdesktop.swinghelper.layer.painter.BufferedPainter;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

import com.jhlabs.image.EmbossFilter;

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

        layer.setPainter(new DisablingPainter<JComponent>());

        frame.add(layer);
        
        frame.add(createToolPanel(), BorderLayout.EAST);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComponent createLayerPanel() {
//        Box panel = Box.createVerticalBox();
        
        JPanel panel = new JPanel(/*new GridLayout(0, 1)*/);
        panel.add(Box.createVerticalGlue());
        panel.add(new JButton("JButton"));
        panel.add(Box.createVerticalGlue());
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(Box.createVerticalGlue());
        JTextField textField = new JTextField("JTextField");
        textField.setMaximumSize(new Dimension(100, 20));
        panel.add(textField);
        panel.add(Box.createVerticalGlue());
        panel.add(new JSlider(0, 100));
        panel.add(Box.createVerticalGlue());
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

    static class DisablingPainter<V extends JComponent> extends BufferedPainter<V> {
        private Effect disablingEffect = new ImageOpEffect(new EmbossFilter()); 
    
        protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
            super.paintToBuffer(g2, l);
            if (!l.isEnabled()) {
                disablingEffect.apply(getBuffer(), g2.getClip());
            } 
        }

        public boolean isIncrementalUpdate(JXLayer<V> l) {
            return l.isEnabled();
        }
    }
}

