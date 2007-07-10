package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.ImageOpFactory;
import org.jdesktop.swinghelper.layer.effect.ImageOpEffect;
import org.jdesktop.swinghelper.layer.painter.BufferedPainter;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.ComponentPainter;
import org.jdesktop.swinghelper.layer.painter.CompoundPainter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class GlassPaneDemo extends JFrame {
    private JXLayer<JComponent> buttonLayer;
    private JButton glassPaneButton;
    private JCheckBox buttonCheckBox;

    public GlassPaneDemo() {
        super("GlassPane demo");
        fillUpTheFrame();

        final ComponentPainter<JComponent> glassPanePainter =
                new ComponentPainter<JComponent>(getLayeredPane(),
                        new ImageOpEffect(ImageOpFactory.getConvolveOp(5)));

        setGlassPane(new JXLayer<JComponent>(glassPanePainter));
        
        glassPaneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // use invokeLater to return the button to not pressed state
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        // it is important to not call update during painting process 
                        glassPanePainter.update();
                        getGlassPane().setVisible(true);
                        JOptionPane.showConfirmDialog(GlassPaneDemo.this,
                                "Do you the effect ?", "Hello", JOptionPane.DEFAULT_OPTION);
                        getGlassPane().setVisible(false);
                    }
                });
            }
        });

        BufferedPainter<JComponent> buttonPainter = new BufferedPainter<JComponent>(
                new DefaultPainter<JComponent>(),
                new ImageOpEffect(ImageOpFactory.getConvolveOp(5)));
        buttonLayer.setPainter(buttonPainter);

        buttonPainter.setEnabled(false);
        buttonPainter.getModel().setIncrementalUpdate(false);

        buttonCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                buttonLayer.getPainter().setEnabled(buttonCheckBox.isSelected());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GlassPaneDemo().setVisible(true);
            }
        });
    }

    private void fillUpTheFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();
        bar.add(new JMenu("Dummy Menu"));
        setJMenuBar(bar);

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
        add(new JScrollPane(table));
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel temp = new JPanel();

        buttonCheckBox = new JCheckBox("\"Disable\" the little button");
        temp.add(buttonCheckBox);

        JButton b = new JButton("I am the little button");
        buttonLayer = new JXLayer<JComponent>(b);
        temp.add(buttonLayer);

        glassPaneButton = new JButton("Show me more with a GlassPane!");
        topPanel.add(temp);
        temp = new JPanel();
        temp.add(glassPaneButton);

        topPanel.add(temp, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        setSize(700, 550);
        setLocationRelativeTo(null);
    }
}
