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
public class TextComponentPainterDemo extends JFrame {
    private JXLayer<JTextComponent> l1;
    private JXLayer<JTextComponent> l2;
    private JXLayer<JTextComponent> l3;
    private JXLayer<JTextComponent> l4;

    public TextComponentPainterDemo() {
        super("Layers for text components");
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
                l3.getPainter().setEnabled(enabled);
                l4.getPainter().setEnabled(enabled);
            }
        });


        optionsMenu.add(paintersItem);
        bar.add(optionsMenu);
        bar.add(new LafMenu());
        setJMenuBar(bar);

        setSize(350, 230);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TextComponentPainterDemo().setVisible(true);
            }
        });
    }

    private JComponent createLayerBox() {
        Box layerBox = Box.createVerticalBox();

        l1 = TextComponentLayerFactory.
                createAlphaLayer(new JTextField(10));

        layerBox.add(Box.createGlue());
        layerBox.add(l1);

        l2 = TextComponentLayerFactory.
                createLockLayer(new JTextField("Outer decoration", 10));
        
        layerBox.add(Box.createGlue());
        layerBox.add(l2);

        l3 = TextComponentLayerFactory.
                createSelectionLayer(new JTextField("Modified selection", 10));
        
        layerBox.add(Box.createGlue());
        layerBox.add(l3);

        l4 = TextComponentLayerFactory.
                createSecretLayer(new JTextField("Hidden digits", 10));

        layerBox.add(Box.createGlue());
        layerBox.add(l4);
        layerBox.add(Box.createGlue());

        return layerBox;
    }

    private Component createTitleBox() {
        Box titleBox = Box.createVerticalBox();
        titleBox.add(Box.createGlue());
        titleBox.add(new JLabel("Type \"hello\""));
        titleBox.add(Box.createGlue());

        JCheckBox cb = new JCheckBox("Make Readonly");
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                l2.getView().setEditable(!l2.getView().isEditable());
            }
        });
        titleBox.add(cb);
        titleBox.add(Box.createGlue());
        titleBox.add(new JLabel("Select the text"));
        titleBox.add(Box.createGlue());
        titleBox.add(new JLabel("Type some digits"));
        titleBox.add(Box.createGlue());
        return titleBox;
    }
}

