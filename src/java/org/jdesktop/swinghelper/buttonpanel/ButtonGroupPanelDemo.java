package org.jdesktop.swinghelper.buttonpanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @author Alexander Potochkin
 * https://swinghelper.dev.java.net/
 */
public class ButtonGroupPanelDemo extends JFrame {
    private ButtonGroup group = new ButtonGroup();

    public ButtonGroupPanelDemo() {
        super("JXButtonGroupPanel demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel topPanel = new JPanel(new GridLayout(1, 0));
        
        final JXButtonGroupPanel checkBoxPanel = createCheckBoxPanel();
        topPanel.add(checkBoxPanel);

        final JXButtonGroupPanel radioGroupPanel = createRadioGroupPanel();
        topPanel.add(radioGroupPanel);
        
        topPanel.add(createRadioButtonPanel());

        add(topPanel);
        add(createButtonPane(), BorderLayout.SOUTH);
        pack();

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem item = new JMenuItem("Clear buttons' selection");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // elegant way is for JXButtonGroupPanel
                radioGroupPanel.clearSelection();
                checkBoxPanel.clearSelection();
                // hack is for 1.5 ButtonGroup
                // in 1.6 ButtonGroup.clearSelection added
                JRadioButton b = new JRadioButton();
                group.add(b);
                b.setSelected(true);
                group.remove(b);
            }
        });
        menu.add(item);
        bar.add(menu);
        setJMenuBar(bar);

        setSize(350, 300);
        setLocationRelativeTo(null);
    }

    private JPanel createRadioButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JRadioButton one = new JRadioButton("One");
        panel.add(one);
        group.add(one);
        JRadioButton two = new JRadioButton("Two");
        panel.add(two);
        group.add(two);
        JRadioButton three = new JRadioButton("Three");
        panel.add(three);
        group.add(three);
        JRadioButton four = new JRadioButton("Three");
        panel.add(four);
        group.add(four);
        one.setSelected(true);
        panel.setBorder(BorderFactory.createTitledBorder("JPanel"));
        return panel;
    }

    private JXButtonGroupPanel createRadioGroupPanel() {
        JXButtonGroupPanel panel = new JXButtonGroupPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(new JRadioButton("One"));
        panel.add(new JRadioButton("Two"));
        panel.add(new JRadioButton("Three"));
        panel.add(new JRadioButton("Four"));
        panel.getButton(0).setSelected(true);
        panel.setBorder(BorderFactory.createTitledBorder("GroupPanel"));
        return panel;
    }

    private JXButtonGroupPanel createCheckBoxPanel() {
        JXButtonGroupPanel panel = new JXButtonGroupPanel(false);
        panel.setLayout(new GridLayout(0, 1));
        panel.add(new JCheckBox("One"));
        panel.add(new JCheckBox("Two"));
        panel.add(new JCheckBox("Three"));
        panel.add(new JCheckBox("Four"));
        panel.setBorder(BorderFactory.createTitledBorder("GroupPanel"));
        return panel;
    }

    private JPanel createButtonPane() {
        JPanel ret = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Does GroupPanel support arrow keys ?");
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JPanel temp = new JPanel();
        temp.add(label);
        ret.add(temp);
        
        JXButtonGroupPanel panel = new JXButtonGroupPanel(false);
        panel.setStopSelectionOnBoundary(false);
        panel.add(new JButton("Yes"));
        panel.add(new JButton("Sure"));
        panel.add(new JButton("Absolutely !"));
        panel.setBorder(BorderFactory.createTitledBorder(null, "GroupPanel", TitledBorder.CENTER, TitledBorder.BOTTOM));
        ret.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ret.add(panel, BorderLayout.SOUTH);
        return ret;
    }

    public static void main(String[] args) throws Exception {
        new ButtonGroupPanelDemo().setVisible(true);
    }
}
