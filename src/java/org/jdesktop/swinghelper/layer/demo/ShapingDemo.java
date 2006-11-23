package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXGlassPane;
import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.shaper.Shaper;

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
public class ShapingDemo {
    
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("ShapingDemo");

        frame.setGlassPane(new JXGlassPane());
        frame.getGlassPane().setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GradientPanel gp = new GradientPanel();
        gp.setLayout(new BorderLayout());
        frame.add(gp);
        
        createGui(gp);

        frame.setSize(440, 220);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    static JComponent getContent() {
        JButton button = new JButton("Shaped button");
        button.setLayout(new FlowLayout());
        button.add(new JButton("Inner button"));
        return button;
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

        final JXLayer layer = new JXLayer(getContent());

        alphaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                layer.setAlpha(alphaSlider.getValue() / 100f);
            }
        });


        layer.setPreferredSize(new Dimension(200, 100));

        Box tempBox = new Box(BoxLayout.Y_AXIS);
        tempBox.setOpaque(false);
        tempBox.add(Box.createVerticalStrut(30));
        tempBox.add(layer);
        tempBox.add(Box.createGlue());
        horBox.add(tempBox);

        horBox.add(Box.createHorizontalStrut(20));

        final TetragonShaper shaper = new TetragonShaper();

        final Timer timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shaper.next();
                layer.repaint();
            }
        });

        layer.setShaper(shaper);

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


    static class TetragonShaper extends Shaper {
        private int x,y;
        public Shape getShape(Component c) {
            if (x > c.getWidth()) {
                x = 0;
            }
            if (y > c.getHeight()) {
                y = 0;
            }
            final GeneralPath path = new GeneralPath();
            path.moveTo(x, 0);
            path.lineTo(c.getWidth(), y);
            path.lineTo(c.getWidth() - x, c.getHeight());
            path.lineTo(0, c.getHeight() - y);
            path.closePath();
            return path;
        }

        public void next() {
            x += 2;
            y += 1;
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
