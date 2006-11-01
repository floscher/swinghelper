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
public class JXButtonPanelDemo extends JFrame {
    private ButtonGroup radioGroup = new ButtonGroup();

    public JXButtonPanelDemo() {
        super("JXButtonPanel demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel topPanel = new JPanel(new GridLayout(1, 0));
        
        final JXButtonPanel radioGroupPanel = createRadioJXButtonPanel();
        topPanel.add(radioGroupPanel);
        
        final JXButtonPanel checkBoxPanel = createCheckBoxJXButtonPanel();
        topPanel.add(checkBoxPanel);

        add(topPanel);
        add(createButtonJXButtonPanel(), BorderLayout.SOUTH);
        pack();

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem item = new JMenuItem("Unselect radioButtons");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // hack for 1.5 
                // in 1.6 ButtonGroup.clearSelection added
                JRadioButton b = new JRadioButton();
                radioGroup.add(b);
                b.setSelected(true);
                radioGroup.remove(b);
            }
        });
        menu.add(item);
        bar.add(menu);
        setJMenuBar(bar);

        setSize(300, 300);
        setLocationRelativeTo(null);
    }

    private JXButtonPanel createRadioJXButtonPanel() {
        JXButtonPanel panel = new JXButtonPanel();
        panel.setLayout(new GridLayout(0, 1));
        JRadioButton one = new JRadioButton("One");
        panel.add(one);
        radioGroup.add(one);
        JRadioButton two = new JRadioButton("Two");
        panel.add(two);
        radioGroup.add(two);
        JRadioButton three = new JRadioButton("Three");
        panel.add(three);
        radioGroup.add(three);
        JRadioButton four = new JRadioButton("Four");
        panel.add(four);
        radioGroup.add(four);
        one.setSelected(true);
        panel.setBorder(BorderFactory.createTitledBorder("JXButtonPanel"));
        return panel;
    }

    private JXButtonPanel createCheckBoxJXButtonPanel() {
        JXButtonPanel panel = new JXButtonPanel();
        panel.setLayout(new GridLayout(0, 1));
        JCheckBox one = new JCheckBox("One");
        panel.add(one);
        JCheckBox two = new JCheckBox("Two");
        panel.add(two);
        JCheckBox three = new JCheckBox("Three");
        panel.add(three);
        JCheckBox four = new JCheckBox("Four");
        panel.add(four);
        panel.setBorder(BorderFactory.createTitledBorder("JXButtonPanel"));
        return panel;
    }

    private JPanel createButtonJXButtonPanel() {
        JPanel ret = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Does JXButtonPanel support arrow keys ?");
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JPanel temp = new JPanel();
        temp.add(label);
        ret.add(temp);
        
        JXButtonPanel panel = new JXButtonPanel();
        panel.setCyclic(true);
        panel.add(new JButton("Yes"));
        panel.add(new JButton("Sure"));
        panel.add(new JButton("Absolutely !"));
        panel.setBorder(BorderFactory.createTitledBorder(null, 
                "JXButtonPanel.setCyclic(true)",
                TitledBorder.CENTER, TitledBorder.BOTTOM));
        ret.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ret.add(panel, BorderLayout.SOUTH);
        return ret;
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXButtonPanelDemo().setVisible(true);
            }
        });         
    }
}
