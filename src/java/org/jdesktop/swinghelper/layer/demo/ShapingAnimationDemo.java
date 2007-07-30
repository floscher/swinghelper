package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.BufferedPainter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

/**
 * @author Alexander Potochkin
 *         
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/
 */
public class ShapingAnimationDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGui();
            }
        });
    }

    private static void createGui() {
        JFrame frame = new JFrame("ShapingDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel gp = new GradientPanel();
        gp.setLayout(new BorderLayout());

        // Since we use translucency and shaping
        // we need to wrap the GradientPanel as well
        // to make it repainted together with their children
        frame.add(new JXLayer<JComponent>(gp));

        createGui(gp);

        frame.setSize(480, 220);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static JXLayer<AbstractButton> getLayer() {
        JButton button = new JButton("Shaped button");
        button.setLayout(new FlowLayout());
        button.add(new JButton("Inner button"));
        return new JXLayer<AbstractButton>(button);
    }

    private static void createGui(JComponent panel) {

        Box horBox = new Box(BoxLayout.X_AXIS);
        horBox.setOpaque(false);
        panel.add(horBox);

        Box vert = new Box(BoxLayout.Y_AXIS);
        vert.setOpaque(false);
        vert.add(Box.createVerticalStrut(40));

        final JCheckBox animationCheckBox = new JCheckBox("Start animation");
        animationCheckBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        animationCheckBox.setOpaque(false);
        vert.add(animationCheckBox);
        vert.add(Box.createVerticalStrut(20));

        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.add(new JLabel("Alpha value", JLabel.CENTER));
        final JSlider alphaSlider = new JSlider(0, 100, 100);
        alphaSlider.setOpaque(false);
        alphaSlider.setPreferredSize(new Dimension(150, 20));
        temp.add(alphaSlider);

        vert.add(temp);

        horBox.add(vert);
        horBox.add(Box.createHorizontalStrut(30));

        final JXLayer<AbstractButton> layer = getLayer();

        final AnimatedPainter painter = new AnimatedPainter();

        layer.setPainter(painter);

        alphaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                float alpha = alphaSlider.getValue() / 100f;
                painter.getModel().setAlpha(alpha);
            }
        });

        layer.setPreferredSize(new Dimension(200, 100));
        layer.setMinimumSize(new Dimension(200, 100));

        JPanel tempPanel = new JPanel(new GridBagLayout());
        tempPanel.setOpaque(false);
        tempPanel.add(layer);

        horBox.add(tempPanel);
        horBox.add(Box.createHorizontalStrut(20));

        final Timer timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                painter.next();
            }
        });

        animationCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (animationCheckBox.isSelected()) {
                    timer.start();
                } else {
                    timer.stop();
                }
            }
        });
    }

    static class AnimatedPainter extends BufferedPainter<AbstractButton> {
        private int x, y;

        protected Shape getClip(JXLayer<AbstractButton> l) {
            if (x > l.getWidth()) {
                x = 0;
            }
            if (y > l.getHeight()) {
                y = 0;
            }
            final GeneralPath path = new GeneralPath();
            path.moveTo(x, 0);
            path.lineTo(l.getWidth(), y);
            path.lineTo(l.getWidth() - x, l.getHeight());
            path.lineTo(0, l.getHeight() - y);
            path.closePath();
            return path;
        }

        public void next() {
            x += 2;
            y += 1;
            fireLayerItemChanged();
        }
    }

    static class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 20, Color.GREEN, 40, 20, Color.YELLOW, true));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}


