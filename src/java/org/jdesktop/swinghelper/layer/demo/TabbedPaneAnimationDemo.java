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
        frame.setJMenuBar(createLafMenuBar());

        final JTabbedPane pane = new JTabbedPane();
        JXLayer l1 = new JXLayer();
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        l1.add(new JButton("Hello"));
        JLabel label = new JLabel("Select the next tab");
        label.setFont(label.getFont().deriveFont(25f));
        l1.add(label);
        pane.add("One", l1);

        JXLayer l2 = new JXLayer();
        l2.add(new JProgressBar(0, 100));
        l2.add(new JCheckBox("Hello"));
        l2.add(new JCheckBox("World"));
        l2.add(new JButton("lala"));
        l2.add(new JButton("lala"));
        l2.add(new JSlider(0, 100));
        JLabel label2 = new JLabel("Do you see the animation ?");
        label2.setFont(label2.getFont().deriveFont(20f));
        l2.add(label2);
        pane.add("Two", l2);

        JXLayer l3 = new JXLayer();
        String[] data = {"one", "two", "three", "four"};
        l3.add(new JList(data));
        l3.add(new JList(data));
        l3.add(new JList(data));
        JLabel label3 = new JLabel("It works !");
        label3.setFont(label3.getFont().deriveFont(25f));
        l3.add(label3);
        label3.setForeground(Color.GREEN.darker());
        pane.add("Three", l3);

        pane.addChangeListener(new TabbedAnimatingChangeListener(100, .05f));

        frame.add(pane);

        frame.setSize(310, 210);
        frame.setLocationRelativeTo(null);
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
            painter = new ComponentPainter(true);
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
    
    private static JMenuBar createLafMenuBar() {
        final JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Change LaF");
        UIManager.LookAndFeelInfo[] lafs = 
                UIManager.getInstalledLookAndFeels();
        for (final UIManager.LookAndFeelInfo laf : lafs) {
            JMenuItem item = new JMenuItem(laf.getName());
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        UIManager.setLookAndFeel(laf.getClassName());
                        SwingUtilities.updateComponentTreeUI(bar.getTopLevelAncestor());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } 
                }
            });
            menu.add(item);
        }
        bar.add(menu);
        return bar;
    }
}
