package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.ImageOpFactory;
import org.jdesktop.swinghelper.layer.effect.ImageOpEffect;
import org.jdesktop.swinghelper.layer.painter.BufferedPainter;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.ComponentPainter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */
public class GlassPaneDemo {
    private static ActionListener action;

    public static void main(String[] args) {
        final JFrame frame = new JFrame("GlassPane demo");

        BufferedPainter p = new BufferedPainter(new ComponentPainter(frame.getLayeredPane()));
        p.setEffects(new ImageOpEffect(ImageOpFactory.getConvolveOp(5)));
        final JXLayer gp = new JXLayer(p);

        frame.setGlassPane(gp);
        action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gp.getPainter().repaint();
                        frame.getGlassPane().setVisible(true);
                        JOptionPane.showConfirmDialog(frame, "Do you the effect ?", "Hello", JOptionPane.DEFAULT_OPTION);
                        frame.getGlassPane().setVisible(false);
                    }
                });
            }
        };

        fillUpTheFrame(frame);

        frame.setResizable(false);
        frame.setVisible(true);
    }

    private static void fillUpTheFrame(final JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();
        bar.add(new JMenu("Dummy Menu"));
        frame.setJMenuBar(bar);

        JTable table = new JTable(new AbstractTableModel() {
            public int getColumnCount() {
                return 3;
            }

            public int getRowCount() {
                return 20;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowIndex + " " + columnIndex;
            }
        });
        frame.add(new JScrollPane(table), BorderLayout.SOUTH);

        final JPanel topPanel = new JPanel(new BorderLayout());
        JPanel temp = new JPanel();

        final JCheckBox checkBox = new JCheckBox("\"Disable\" the little button");
        temp.add(checkBox);
        final JButton b = new JButton("I am the little button");

        final ImageOpEffect effect = new ImageOpEffect(ImageOpFactory.getConvolveOp(5));
        effect.setEnabled(false);
        BufferedPainter p = new BufferedPainter(new DefaultPainter());
        p.setEffects(effect);

        final JXLayer layer = new JXLayer(b, p);
        temp.add(layer);

        JButton button = new JButton("Show me more with a GlassPane!");
        button.addActionListener(action);

        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                effect.setEnabled(checkBox.isSelected());
            }
        });

        topPanel.add(temp);
        temp = new JPanel();
        temp.add(button);
        topPanel.add(temp, BorderLayout.SOUTH);
        frame.add(topPanel);

        frame.setSize(700, 550);
        frame.setLocationRelativeTo(null);
    }
}
