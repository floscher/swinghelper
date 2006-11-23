package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.ComponentPainter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class TabbedPaneAnimationDemo {
    public static void main(String[] args) {

        JFrame frame = new JFrame("TabbedPane demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JTabbedPane pane = new JTabbedPane();
        JXLayer l1 = new JXLayer();
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        JLabel label = new JLabel("It works !");
        label.setFont(label.getFont().deriveFont(30f));
        l1.add(label);
        pane.add("One", l1);

        JXLayer l2 = new JXLayer();
        l2.add(new JProgressBar(0, 100));
        l2.add(new JLabel("Second tab !"));
        l2.add(new JButton("lala"));
        l2.add(new JButton("lala"));
        l2.add(new JButton("lala"));
        l2.add(new JButton("lala"));
        l2.add(new JSlider(0, 100));
        l2.add(new JProgressBar(0, 100));
        pane.add("Two", l2);
        
        JXLayer l3 = new JXLayer();
        String[] data = {"one", "two", "three", "four"};
        l3.add(new JList(data));
        l3.add(new JList(data));
        l3.add(new JList(data));
        l3.add(new JButton("One more very big button"));
        pane.add("Three", l3);
        
        pane.addChangeListener(new TabbedAnimatingChangeListener(100, .05f));
        
        frame.add(pane);

        frame.setSize(280, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    static class TabbedAnimatingChangeListener implements ChangeListener {
        private int index;
        private Timer timer;
        private ComponentPainter painter;
        private JTabbedPane pane;
        private float delta;

        public TabbedAnimatingChangeListener() {
            this(100, .1f);
        }

        public TabbedAnimatingChangeListener(int delay, final float delta) {
            this.delta = delta;
            painter = new ComponentPainter() {
                public void paint(Graphics2D g2, Component c) {
                    Composite composite = g2.getComposite();
                    if (composite instanceof AlphaComposite) {
                        AlphaComposite ac = (AlphaComposite) composite;
                        g2.setComposite(
                                AlphaComposite.getInstance(
                                        AlphaComposite.SRC_OVER, 1 - ac.getAlpha()));
                    }
                    super.paint(g2, c);
                }
            };            
            timer = new Timer(delay, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JXLayer l = (JXLayer) pane.getSelectedComponent();
                    painter.repaint();
                    if (l.getAlpha() >= 1 - delta) {                        
                        l.setAlpha(1);
                        timer.stop();
                        return;
                    }
                    l.setAlpha(l.getAlpha() + delta);
                }
            });
        }

        public float getDelta() {
            return delta;
        }

        public void setDelta(float delta) {
            if (delta <= 0 || delta > 1) {
                throw new IllegalArgumentException();
            }
            this.delta = delta;
        }

        public int getDelay() {
            return timer.getDelay();
        }

        public void setDelay(int delay) {
            timer.setDelay(delay);
        }

        public void stateChanged(ChangeEvent e) {
            pane = (JTabbedPane) e.getSource();
            JXLayer layer = (JXLayer) pane.getSelectedComponent();
            JXLayer oldLayer = (JXLayer) pane.getComponentAt(index);
            layer.setAlpha(1 - oldLayer.getAlpha());
            painter.setComponent(pane.getComponentAt(index));
            oldLayer.setBackgroundPainter(null);
            layer.setBackgroundPainter(painter);            
            timer.start();
            index = pane.getSelectedIndex();
        }
    }
}
