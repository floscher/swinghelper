package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXContainer;
import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.ComponentPainter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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

        final MagicGlassPane gp = new MagicGlassPane();
        gp.setBufferedImageOp(BufferedImageOps.getConvolveOp(5));

        action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gp.apply(frame);
                        JOptionPane.showConfirmDialog(frame, "Do you the effect ?", "Hello", JOptionPane.DEFAULT_OPTION);
                        gp.reset();
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
        final JXLayer layer = new JXLayer(b);
        temp.add(layer);

        JButton button = new JButton("Show me more with a GlassPane!");
        button.addActionListener(action);

        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox.isSelected()) {
                    layer.setBufferedImageOp(org.jdesktop.swinghelper.layer.demo.BufferedImageOps.getConvolveOp(3));
                    layer.setEnabled(false);
                } else {
                    layer.setBufferedImageOp(null);
                    layer.setEnabled(true);
                }
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

    static class MagicGlassPane extends JXContainer {
        private ComponentPainter componentPainter;
        private Component oldGlassPane;

        public MagicGlassPane() {
            super(new BorderLayout());
            final JXContainer content = new JXContainer();
            componentPainter = new ComponentPainter();
            content.setForegroundPainter(componentPainter);
            add(content);
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    componentPainter.repaint();
                }
            });
        }

        public void apply(RootPaneContainer rpc) {
            if (rpc == null) {
                throw new IllegalArgumentException();
            }
            componentPainter.setComponent(rpc.getLayeredPane());
            oldGlassPane = rpc.getGlassPane();
            rpc.setGlassPane(this);
            setVisible(true);
        }

        public void reset() {
            componentPainter.setComponent(null);
            JRootPane rp = getRootPane();
            if (rp != null && oldGlassPane != null) {
                rp.setGlassPane(oldGlassPane);
            }
        }
    }
}