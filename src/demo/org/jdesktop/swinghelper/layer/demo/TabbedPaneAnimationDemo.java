package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;
import org.jdesktop.swinghelper.layer.painter.ComponentPainter;
import org.jdesktop.swinghelper.layer.painter.model.PainterModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Alexander Potochkin
 *         
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/
 */
public class TabbedPaneAnimationDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGui();
            }
        });
    }

    private static void createGui() {
        JFrame frame = new JFrame("TabbedPane demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(LafMenu.createMenuBar());

        final JTabbedPane pane = new JTabbedPane();
        
        JPanel p1 = new JPanel();
        p1.add(new JButton("Hello"));
        p1.add(new JButton("Hello"));
        p1.add(new JButton("Hello"));
        p1.add(new JButton("Hello"));
        p1.add(new JButton("Hello"));
        p1.add(new JButton("Hello"));
        JLabel label = new JLabel("Select the next tab");
        label.setFont(label.getFont().deriveFont(25f));
        p1.add(label);
        pane.add("One", new JXLayer<JComponent>(p1));

        JPanel p2 = new JPanel();
        p2.add(new JProgressBar(0, 100));
        p2.add(new JCheckBox("Hello"));
        p2.add(new JCheckBox("World"));
        p2.add(new JButton("lala"));
        p2.add(new JButton("lala"));
        p2.add(new JSlider(0, 100));
        JLabel label2 = new JLabel("Do you see the animation ?");
        label2.setFont(label2.getFont().deriveFont(20f));
        p2.add(label2);
        pane.add("Two", new JXLayer<JComponent>(p2));

        JPanel p3 = new JPanel();
        String[] data = {"one", "two", "three", "four"};
        p3.add(new JList(data));
        p3.add(new JList(data));
        p3.add(new JList(data));
        JLabel label3 = new JLabel("It works !");
        label3.setFont(label3.getFont().deriveFont(25f));
        p3.add(label3);
        label3.setForeground(Color.GREEN.darker());
        pane.add("Three", new JXLayer<JComponent>(p3));

        pane.addChangeListener(new TabbedAnimatingChangeListener(50, .05f));

        frame.add(pane);

        frame.setSize(310, 230);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    static class TabbedAnimatingChangeListener implements ChangeListener {
        private int index;
        private Timer timer;
        private ComponentPainter<JComponent> painter;
        private float delta;

        public TabbedAnimatingChangeListener() {
            this(100, .1f);
        }

        public TabbedAnimatingChangeListener(int delay, final float delta) {
            this.delta = delta;            
            painter = new AnimationPainter();            
            
            timer = new Timer(delay, new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    painter.update();
                    PainterModel model = painter.getModel();

                    if (model.getAlpha() <= delta) {
                        model.setAlpha(0);
                        timer.stop();
                        return;
                    }
                    
                    model.setAlpha(model.getAlpha() - delta);
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
            JTabbedPane pane = (JTabbedPane) e.getSource();
            JXLayer<JComponent> layer = 
                    (JXLayer<JComponent>) pane.getSelectedComponent();
            JXLayer<JComponent> oldLayer = 
                    (JXLayer<JComponent>) pane.getComponentAt(index);
            
            PainterModel model = painter.getModel();
            model.setAlpha(1 - model.getAlpha());
            
            // set oldLayer as a foreground
            painter.setComponent(oldLayer);            
            // swap painters
            oldLayer.setPainter(layer.getPainter());
            layer.setPainter(painter);
            
            painter.update();            
            timer.start();
            index = pane.getSelectedIndex();
        }
    }
    
    static class AnimationPainter extends ComponentPainter<JComponent> {
        public AnimationPainter() {
            getModel().setAlpha(0);
        }

        public void paint(Graphics2D g2, JXLayer<JComponent> l) {
            // paint the layer
            l.paint(g2);
            // paint the old layer with diminishing alpha
            super.paint(g2, l);            
        }
    }
}
