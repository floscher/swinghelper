package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.effect.Effect;
import org.jdesktop.swinghelper.layer.effect.ImageOpEffect;
import org.jdesktop.swinghelper.layer.painter.BufferedPainter;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.jhlabs.image.EmbossFilter;

/**
 * @author Alexander Potochkin
 *         <p/>
 *         https://swinghelper.dev.java.net/
 *         http://weblogs.java.net/blog/alexfromsun/
 */
public class DisabledLayerDemo extends JFrame {
    private JXLayer<JComponent> layer;
    private Painter<JComponent> simplePainter = new SimplePainter<JComponent>();
    private Painter<JComponent> imageOpPainter = new ImageOpPainter<JComponent>();

    private JCheckBoxMenuItem disablingMenuItem = new JCheckBoxMenuItem(new AbstractAction("Disable the layer") {
        public void actionPerformed(ActionEvent e) {
            layer.setEnabled(!layer.isEnabled());
        }
    });

    private JRadioButtonMenuItem simpleItem = new JRadioButtonMenuItem("Simple effect");
    private JRadioButtonMenuItem imageOpItem = new JRadioButtonMenuItem("Emboss effect");

    public DisabledLayerDemo() {
        super("Disabled/enabled layer demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layer = new JXLayer<JComponent>(createLayerPanel());
        layer.setPainter(simplePainter);
        add(layer);
        add(createToolPanel(), BorderLayout.EAST);
        setJMenuBar(createMenuBar());
        setSize(430, 230);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws Exception {
        new DisabledLayerDemo().setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenu menu = new JMenu("Menu");

        disablingMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));

        menu.add(disablingMenuItem);
        menu.addSeparator();


        simpleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
        simpleItem.setSelected(true);
        menu.add(simpleItem);

        imageOpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
        menu.add(imageOpItem);

        ButtonGroup group = new ButtonGroup();
        group.add(simpleItem);
        group.add(imageOpItem);

        ItemListener menuListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                layer.setPainter(simpleItem.isSelected() ? simplePainter : imageOpPainter);
            }
        };

        simpleItem.addItemListener(menuListener);
        imageOpItem.addItemListener(menuListener);

        JMenuBar bar = new JMenuBar();
        bar.add(menu);
        return bar;
    }

    private JComponent createLayerPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        JTextField textField = new JTextField("JTextField");
        textField.setMaximumSize(new Dimension(100, 20));
        panel.add(textField);
        panel.add(new JScrollPane(new JTextArea(5, 20)));
        panel.add(new JButton("Have a nice day"));
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private JComponent createToolPanel() {
        JComponent box = Box.createVerticalBox();
        JCheckBox button = new JCheckBox(disablingMenuItem.getText());
        button.setModel(disablingMenuItem.getModel());
        box.add(Box.createGlue());
        box.add(button);
        box.add(Box.createGlue());
        JRadioButton simple = new JRadioButton("Simple effect");
        simple.setModel(simpleItem.getModel());
        box.add(simple);
        JRadioButton emboss = new JRadioButton("Emboss effect");
        emboss.setModel(imageOpItem.getModel());
        box.add(emboss);
        box.add(Box.createGlue());
        return box;
    }

    static class SimplePainter<V extends JComponent> extends DefaultPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            super.paint(g2, l);
            if (!l.isEnabled()) {
                g2.setColor(Color.RED);
                g2.setFont(g2.getFont().deriveFont(30f));
                g2.drawString("Disabled", 20, 100);
            }
        }
    }

    static class ImageOpPainter<V extends JComponent> extends BufferedPainter<V> {
        private Effect disablingEffect = new ImageOpEffect(new EmbossFilter());

        protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
            super.paintToBuffer(g2, l);
            if (!l.isEnabled()) {
                disablingEffect.apply(getBuffer(), g2.getClip());
            }
        }

        // Child components of a disabled layer are disalbed as well, 
        // so we can speed the painting up; 
        // moreover EmbossFilter doesn't like incremental updates           
        public boolean isIncrementalUpdate(JXLayer<V> l) {
            return l.isEnabled();
        }
    }
}

