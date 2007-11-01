package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.demo.util.LafMenu;
import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class DebugLayerDemo extends JFrame {

    public DebugLayerDemo() {
        super("DebugLayerDemo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tb = new JTabbedPane();
        tb.setTabPlacement(JTabbedPane.BOTTOM);
        tb.addTab("Components", createButtonPanel());
        tb.addTab("Table", createTable());
        tb.addTab("Tree", new JTree());
        JXLayer<JComponent> layer = new JXLayer<JComponent>(tb);
        add(layer);

        final DebugPainter<JComponent> dp = new DebugPainter<JComponent>();
        layer.setPainter(dp);
        
        JMenuBar bar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        bar.add(optionsMenu);
        final JCheckBoxMenuItem startItem = new JCheckBoxMenuItem("Start debugging", true);
        startItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        optionsMenu.add(startItem);
        startItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                dp.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        bar.add(optionsMenu);
        bar.add(new LafMenu());
        setJMenuBar(bar);
        
        setSize(400, 350);
        setLocationRelativeTo(null);
    }

    private JComponent createTable() {
        return new JScrollPane(new JTable(new AbstractTableModel() {

            public int getColumnCount() {
                return 10;
            }

            public int getRowCount() {
                return 20;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return "" + rowIndex;
            }
        }));
    }

    private JComponent createButtonPanel() {
        Box box = Box.createVerticalBox();
        box.add(Box.createGlue());
        addToBox(box, new JButton("JButton"));
        addToBox(box, new JRadioButton("JRadioButton"));
        addToBox(box, new JCheckBox("JCheckBox"));
        addToBox(box, new JTextField(10));
        String[] str = {"One", "Two", "Three"};
        addToBox(box, new JComboBox(str));
        addToBox(box, new JSlider(0, 100));
        return box;
    }
    
    private void addToBox(Box box, JComponent c) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(c);
        box.add(panel);
        box.add(Box.createGlue());
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DebugLayerDemo().setVisible(true);
            }
        });
    }
}
