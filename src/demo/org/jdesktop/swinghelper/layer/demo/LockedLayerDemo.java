package org.jdesktop.swinghelper.layer.demo;

import com.jhlabs.image.BlurFilter;
import com.jhlabs.image.EmbossFilter;
import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;
import org.jdesktop.swinghelper.layer.effect.Effect;
import org.jdesktop.swinghelper.layer.effect.ImageOpEffect;
import org.jdesktop.swinghelper.layer.painter.AbstractPainter;
import org.jdesktop.swinghelper.layer.painter.BufferedPainter;
import org.jdesktop.swinghelper.layer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImageOp;

/**
 * @author Alexander Potochkin
 *         <p/>
 *         https://swinghelper.dev.java.net/
 *         http://weblogs.java.net/blog/alexfromsun/
 */
public class LockedLayerDemo extends JFrame {
    private JXLayer<JComponent> layer;
    private Painter<JComponent> translucentPainter = new TranslucentPainter<JComponent>();
    private Painter<JComponent> blurPainter = new ImageOpPainter<JComponent>(new BlurFilter());
    private Painter<JComponent> embossPainter = new ImageOpPainter<JComponent>(new EmbossFilter());

    private JCheckBoxMenuItem disablingItem = 
            new JCheckBoxMenuItem(new AbstractAction("Lock the layer") {
                
        public void actionPerformed(ActionEvent e) {            
            layer.setLocked(!layer.isLocked());
            
            translucentItem.setEnabled(layer.isLocked());
            blurItem.setEnabled(layer.isLocked());
            embossItem.setEnabled(layer.isLocked());
        }
    });

    private JRadioButtonMenuItem translucentItem = new JRadioButtonMenuItem("Translucent color");
    private JRadioButtonMenuItem blurItem = new JRadioButtonMenuItem("Blur effect");
    private JRadioButtonMenuItem embossItem = new JRadioButtonMenuItem("Emboss effect");

    public LockedLayerDemo() {
        super("Locked/unlocked layer demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layer = new JXLayer<JComponent>(createLayerPanel());
        layer.setPainter(translucentPainter);
        add(layer);
        add(createToolPanel(), BorderLayout.EAST);
        setJMenuBar(createMenuBar());
        setSize(380, 300);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws Exception {
        new LockedLayerDemo().setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenu menu = new JMenu("Menu");

        disablingItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));

        menu.add(disablingItem);
        menu.addSeparator();

        translucentItem.setSelected(true);
        translucentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_MASK));
        menu.add(translucentItem);

        blurItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_MASK));
        menu.add(blurItem);

        embossItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_MASK));
        menu.add(embossItem);

        ButtonGroup group = new ButtonGroup();
        group.add(translucentItem);
        group.add(blurItem);
        group.add(embossItem);
        
        translucentItem.setEnabled(false);
        blurItem.setEnabled(false);
        embossItem.setEnabled(false);

        ItemListener menuListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (translucentItem.isSelected()) {
                    layer.setPainter(translucentPainter);
                } else if(blurItem.isSelected()) {
                    layer.setPainter(blurPainter);
                } else if (embossItem.isSelected()) {
                    layer.setPainter(embossPainter);
                }
            }
        };

        translucentItem.addItemListener(menuListener);
        blurItem.addItemListener(menuListener);
        embossItem.addItemListener(menuListener);
        
        JMenuBar bar = new JMenuBar();
        bar.add(menu);

        bar.add(new LafMenu());
        return bar;
    }

    private JComponent createLayerPanel() {
        JComponent panel = new JPanel();
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField(15));
        panel.add(new JButton("Have a nice day"));
        panel.add(new JTextField(15));
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField(15));
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private JComponent createToolPanel() {
        JComponent box = Box.createVerticalBox();
        JCheckBox button = new JCheckBox(disablingItem.getText());
        button.setModel(disablingItem.getModel());
        box.add(Box.createGlue());
        box.add(button);
        box.add(Box.createGlue());
        JRadioButton translucent = new JRadioButton("Translucent");
        translucent.setModel(translucentItem.getModel());
        box.add(translucent);
        JRadioButton blur = new JRadioButton("Blur effect");
        blur.setModel(blurItem.getModel());
        box.add(blur);
        JRadioButton emboss = new JRadioButton("Emboss effect");
        emboss.setModel(embossItem.getModel());
        box.add(emboss);
        box.add(Box.createGlue());
        return box;
    }

    static class TranslucentPainter<V extends JComponent> extends AbstractPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            l.paint(g2);
            if (l.isLocked()) {
                g2.setColor(new Color(0, 128, 128, 128));
                g2.fillRect(0, 0, l.getWidth(), l.getHeight());
            }
        }
    }

    static class ImageOpPainter<V extends JComponent> extends BufferedPainter<V> {
        private Effect disablingEffect;

        public ImageOpPainter(BufferedImageOp disablingEffect) {
            this.disablingEffect = new ImageOpEffect(disablingEffect);
        }

        protected void paintToBuffer(Graphics2D g2, JXLayer<V> l) {
            super.paintToBuffer(g2, l);
            if (l.isLocked()) {
                disablingEffect.apply(getBuffer(), g2.getClip());
            }
        }

        // Child components of a locked layer are locked as well, 
        // so we can speed the painting up; 
        // moreover EmbossFilter doesn't like incremental updates           
        public boolean isIncrementalUpdate(JXLayer<V> l) {
            return !l.isLocked();
        }
    }
}

