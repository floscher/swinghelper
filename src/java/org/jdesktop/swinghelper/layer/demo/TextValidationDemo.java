package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Alexander Potochkin
 *         
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/
 */
public class TextValidationDemo extends JFrame {
    private JXLayer<JTextComponent> l1;
    private JXLayer<JTextComponent> l2;

    public TextValidationDemo() {
        super("Validation layers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 10));
        panel.add(createLayerBox());
        panel.add(createTitleBox());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        add(panel);

        JMenuBar bar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JCheckBoxMenuItem paintersItem = new JCheckBoxMenuItem("Disable painters");
        paintersItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        paintersItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                boolean enabled = e.getStateChange() == ItemEvent.DESELECTED;
                l1.getPainter().setEnabled(enabled);
                l2.getPainter().setEnabled(enabled);
            }
        });
        optionsMenu.add(paintersItem);
        bar.add(optionsMenu);
        bar.add(new LafMenu());
        setJMenuBar(bar);
        setSize(300, 150);
        setLocationRelativeTo(null);
    }

    private JComponent createLayerBox() {
        Box layerBox = Box.createVerticalBox();

        l1 = ValidationLayerFactory.createValidationLayer(new JTextField(10));

        layerBox.add(Box.createGlue());
        layerBox.add(l1);

        l2 = ValidationLayerFactory.createIconLayer(new JTextField(10));
        
        layerBox.add(Box.createGlue());
        layerBox.add(l2);
        return layerBox;
    }

    private Component createTitleBox() {
        Box titleBox = Box.createVerticalBox();
        titleBox.add(new JLabel("type \"JXLayer\""));
        titleBox.add(new JLabel("and see the result"));
        titleBox.add(Box.createGlue());
        return titleBox;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TextValidationDemo().setVisible(true);
            }
        });
    }
}

