package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * @author Alexander Potochkin
 *         
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/
 */
public class BorderDemo {

    private static void createGui() {
        final JFrame frame = new JFrame("Custom border test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JButton button1 = new JButton("Hello");
        button1.setToolTipText("Unmodified JButton");
        frame.add(button1);
        
        JButton button2 = new JButton("Hello");
        button2.setToolTipText("custom border");
        button2.setBorder(getBorder());
        frame.add(button2);

        JButton button3 = new JButton("Hello");
        button3.setToolTipText("compound border (mixed button's and custom ones)");
        CompoundBorder cb = new CompoundBorder(getBorder(), button3.getBorder());
        button3.setBorder(cb);
        frame.add(button3);

        JButton button4 = new JButton("Hello");
        button4.setToolTipText("JXLayer with custom border");
        JXLayer<JButton> layer = new JXLayer<JButton>(button4);
        layer.setBorder(getBorder());
        frame.add(layer);

        frame.setJMenuBar(LafMenu.createMenuBar());
        
        frame.setSize(300, 130);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static Border getBorder() {
        return BorderFactory.createLineBorder(Color.GREEN, 5);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGui();
            }
        });
    }
}
