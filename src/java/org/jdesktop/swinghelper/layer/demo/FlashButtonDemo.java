package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;
import org.jdesktop.swinghelper.layer.painter.AbstractPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class FlashButtonDemo {
    private static void createGui() {
        JFrame frame = new JFrame("FlashButtonDemo");
        frame.setJMenuBar(LafMenu.createMenuBar());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JXLayer<AbstractButton> l = new JXLayer<AbstractButton>(new JButton("Hello"));
        frame.add(l);

        l.setPainter(new FlashPainter());

        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class FlashPainter extends AbstractPainter<AbstractButton> {
        private Timer timer;
        private double scale;
        private boolean rollover;

        public FlashPainter() {
            scale = 1;
            timer = new Timer(50, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (scale > 2) {
                        timer.stop();
                        scale = 1;
                    } else {
                        scale += .2;
                    }
                    fireLayerItemChanged();
                }
            });
        }

        public void paint(Graphics2D g2, JXLayer<AbstractButton> l) {
            configure(g2, l);
            l.paint(g2);
            updateGraphics(g2, l);
            l.paint(g2);
        }

        private void updateGraphics(Graphics2D g2, JXLayer<AbstractButton> l) {
            boolean currRollover = l.getView().getModel().isRollover();
            if (currRollover && !rollover) {
                if (!timer.isRunning()) {
                    timer.start();
                }
            }
            rollover = currRollover;
            if (timer.isRunning()) {
                g2.setTransform(getScaleTransform(l));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
            }
        }

        private AffineTransform getScaleTransform(JComponent c) {
            AffineTransform t = AffineTransform.getScaleInstance(scale, scale);
            int width = c.getWidth();
            double xt = ((width * scale) - width) / (2 * scale);
            int height = c.getHeight();
            double yt = ((height * scale) - height) / (2 * scale);
            t.translate(-xt, -yt);
            return t;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGui();
            }
        });
    }
}                               