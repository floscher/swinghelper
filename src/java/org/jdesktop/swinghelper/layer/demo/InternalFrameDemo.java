package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.effect.Effect;
import org.jdesktop.swinghelper.layer.effect.ImageOpEffect;
import org.jdesktop.swinghelper.layer.effect.NullEffect;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;
import org.jdesktop.swinghelper.layer.demo.util.ImageOpFactory;
import org.jdesktop.swinghelper.layer.painter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * @author Jeff Dinkins
 * @author Alexander Potochkin
 *         <p/>
 *         https://swinghelper.dev.java.net/
 *         http://weblogs.java.net/blog/alexfromsun/
 */
public class InternalFrameDemo extends JPanel {
    int windowCount = 0;
    JDesktopPane desktop = null;

    ImageIcon icon1, icon2, icon3, icon4;
    ImageIcon smIcon1, smIcon2, smIcon3, smIcon4;

    public Integer FIRST_FRAME_LAYER = new Integer(1);
    public Integer DEMO_FRAME_LAYER = new Integer(2);
    public Integer PALETTE_LAYER = new Integer(3);

    public int FRAME0_X = 15;
    public int FRAME0_Y = 280;

    public int FRAME0_WIDTH = 320;
    public int FRAME0_HEIGHT = 230;

    public int FRAME_WIDTH = 225;
    public int FRAME_HEIGHT = 150;

    public int PALETTE_X = 400;
    public int PALETTE_Y = 20;

    public int PALETTE_WIDTH = 300;
    public int PALETTE_HEIGHT = 350;

    // Premade convenience dimensions, for use wherever you need 'em.
    public static Dimension HGAP2 = new Dimension(2, 1);
    public static Dimension VGAP2 = new Dimension(1, 2);

    public static Dimension HGAP5 = new Dimension(5, 1);
    public static Dimension VGAP5 = new Dimension(1, 5);

    public static Dimension HGAP10 = new Dimension(10, 1);
    public static Dimension VGAP10 = new Dimension(1, 10);

    public static Dimension HGAP15 = new Dimension(15, 1);
    public static Dimension VGAP15 = new Dimension(1, 15);

    public static Dimension HGAP20 = new Dimension(20, 1);
    public static Dimension VGAP20 = new Dimension(1, 20);

    public static Dimension HGAP25 = new Dimension(25, 1);
    public static Dimension VGAP25 = new Dimension(1, 25);

    public static Dimension HGAP30 = new Dimension(30, 1);
    public static Dimension VGAP30 = new Dimension(1, 30);

    JCheckBox windowResizable = null;
    JCheckBox windowClosable = null;
    JCheckBox windowIconifiable = null;
    JCheckBox windowMaximizable = null;

    JTextField windowTitleField = null;
    JLabel windowTitleLabel = null;


    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {

        final JFrame frame = new JFrame("JXLayer demo");
        InternalFrameDemo demo = new InternalFrameDemo();

        final CoverPainter p = new CoverPainter();

        final JXLayer<JComponent> layer = new JXLayer<JComponent>(demo, p);

        JMenuBar bar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        final JMenuItem painterItem = new JCheckBoxMenuItem("Set painters and alpha");
        painterItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
        optionsMenu.add(painterItem);

        painterItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setShowPainters(painterItem.isSelected());
            }
        });

        optionsMenu.addSeparator();

        ButtonGroup group = new ButtonGroup();
        final JMenuItem defaultItem = new JRadioButtonMenuItem("No BufferedImageOp");
        defaultItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        optionsMenu.add(defaultItem);
        group.add(defaultItem);
        defaultItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (defaultItem.isSelected()) {
                    p.setEffect(new NullEffect());
                }
            }
        });
        defaultItem.setSelected(true);

        final JMenuItem invertItem = new JRadioButtonMenuItem("Apply InvertBufferedImageOp");
        invertItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_MASK));
        optionsMenu.add(invertItem);
        group.add(invertItem);
        invertItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (invertItem.isSelected()) {
                    p.setEffect(new ImageOpEffect(ImageOpFactory.getInvertColorOp()));
                }
            }
        });

        final JMenuItem posterItem = new JRadioButtonMenuItem("Apply GrayScaleImageOp");
        posterItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_MASK));
        optionsMenu.add(posterItem);
        group.add(posterItem);
        posterItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (posterItem.isSelected()) {
                    p.setEffect(new ImageOpEffect(ImageOpFactory.getGrayScaleOp()));
                }
            }
        });

        bar.add(optionsMenu);
        bar.add(new LafMenu());
        frame.setJMenuBar(bar);


        frame.add(layer);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }

    static class CoverPainter extends AbstractPainter<JComponent> {
        private boolean showPainters;
        private Painter<JComponent> backgroundPainter;
        private Effect effect;
        private BufferedPainter<JComponent> mainPainter;
        private Painter<JComponent> foregroundPainter;

        public CoverPainter() {
            Painter backgroundImpl = new AbstractPainter() {
                public void paint(Graphics2D g2, JXLayer l) {
                    g2.setPaint(new GradientPaint(0, 0, Color.BLACK, 50, 50, Color.RED, true));
                    g2.fillOval(0, 0, l.getWidth(), l.getHeight());
                }
            };
            backgroundPainter = new CompoundPainter<JComponent>(
                    new BackgroundPainter(),
                    backgroundImpl);
            mainPainter = new BufferedPainter<JComponent>(new DefaultPainter<JComponent>());
            effect = new ImageOpEffect();
            mainPainter.setEffects(effect);

            foregroundPainter = new AbstractPainter() {
                public void paint(Graphics2D g2, JXLayer l) {
                    g2.setColor(Color.GREEN.darker());
                    Font font = g2.getFont().deriveFont(40f);
                    g2.setFont(font);
                    g2.drawString("ForegroundPainter",
                            l.getWidth() / 2, l.getHeight() / 2 + 150);
                }
            };
        }

        public Effect getEffect() {
            return effect;
        }

        public void setEffect(Effect effect) {
            this.effect = effect;
            mainPainter.setEffects(effect);
            fireStateChanged();
        }

        public boolean isShowPainters() {
            return showPainters;
        }

        public void setShowPainters(boolean showPainters) {
            this.showPainters = showPainters;
            if (showPainters) {
                mainPainter.getModel().setAlpha(.7f);
            } else {
                mainPainter.getModel().setAlpha(1);
            }
            fireStateChanged();
        }

        public void paint(Graphics2D g2, JXLayer<JComponent> l) {
            if (showPainters) {
                backgroundPainter.paint(g2, l);
            }
            mainPainter.paint(g2, l);
            if (showPainters) {
                foregroundPainter.paint(g2, l);
            }
        }
    }

    /**
     * InternalFrameDemo Constructor
     */
    public InternalFrameDemo() {

        // preload all the icons we need for this demo
        icon1 = createImageIcon("flower.gif", getString("InternalFrameDemo.fish"));
        icon2 = createImageIcon("butterfly.gif", getString("InternalFrameDemo.moon"));
        icon3 = createImageIcon("flower2.gif", getString("InternalFrameDemo.sun"));
        icon4 = createImageIcon("earth.gif", getString("InternalFrameDemo.cab"));

        smIcon1 = createImageIcon("flower_small.gif", getString("InternalFrameDemo.fish"));
        smIcon2 = createImageIcon("butterfly_small.gif", getString("InternalFrameDemo.moon"));
        smIcon3 = createImageIcon("flower2_small.gif", getString("InternalFrameDemo.sun"));
        smIcon4 = createImageIcon("earth_small.gif", getString("InternalFrameDemo.cab"));

        // Create the desktop pane
        desktop = new JDesktopPane();

        //Todo: this is important
        desktop.setDragMode(2);

        setLayout(new BorderLayout());
        add(desktop, BorderLayout.CENTER);

        // Create the "frame maker" palette
        createInternalFramePalette();

        // Create an initial internal frame to show
        JInternalFrame frame1 = createInternalFrame(icon1, FIRST_FRAME_LAYER, 1, 1);
        frame1.setBounds(FRAME0_X, FRAME0_Y, FRAME0_WIDTH, FRAME0_HEIGHT);

        // Create four more starter windows
        createInternalFrame(icon1, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
        createInternalFrame(icon3, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
        createInternalFrame(icon4, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
        createInternalFrame(icon2, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
    }


    /**
     * Create an internal frame and add a scrollable imageicon to it
     */
    public JInternalFrame createInternalFrame(Icon icon, Integer layer, int width, int height) {
        JInternalFrame jif = new JInternalFrame();

        if (!windowTitleField.getText().equals(getString("InternalFrameDemo.frame_label"))) {
            jif.setTitle(windowTitleField.getText() + "  ");
        } else {
            jif = new JInternalFrame(getString("InternalFrameDemo.frame_label") + " " + windowCount + "  ");
        }

        // set properties
        jif.setClosable(windowClosable.isSelected());
        jif.setMaximizable(windowMaximizable.isSelected());
        jif.setIconifiable(windowIconifiable.isSelected());
        jif.setResizable(windowResizable.isSelected());

        jif.setBounds(20 * (windowCount % 10), 20 * (windowCount % 10), width, height);
        jif.setContentPane(new ImageScroller(this, icon, 0, windowCount));

        windowCount++;

        desktop.add(jif, layer);

        // Set this internal frame to be selected

        try {
            jif.setSelected(true);
        } catch (java.beans.PropertyVetoException e2) {
            e2.printStackTrace();
        }

        jif.show();

        return jif;
    }

    JInternalFrame palette;

    public JInternalFrame createInternalFramePalette() {
        palette = new JInternalFrame(getString("InternalFrameDemo.palette_label"));
        palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        palette.getContentPane().setLayout(new BorderLayout());
        palette.setBounds(PALETTE_X, PALETTE_Y, PALETTE_WIDTH, PALETTE_HEIGHT);
        palette.setResizable(true);
        palette.setIconifiable(true);
        desktop.add(palette, PALETTE_LAYER);

        // *************************************
        // * Create create frame maker buttons *
        // *************************************
        JButton b1 = new JButton(smIcon1);

        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton b2 = new JButton(smIcon2);
        JButton b3 = new JButton(smIcon3);
        JButton b4 = new JButton(smIcon4);

        // add frame maker actions
        b1.addActionListener(new ShowFrameAction(this, icon1));
        b2.addActionListener(new ShowFrameAction(this, icon2));
        b3.addActionListener(new ShowFrameAction(this, icon3));
        b4.addActionListener(new ShowFrameAction(this, icon4));

        // add frame maker buttons to panel
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel buttons1 = new JPanel();
        buttons1.setLayout(new BoxLayout(buttons1, BoxLayout.X_AXIS));

        JPanel buttons2 = new JPanel();
        buttons2.setLayout(new BoxLayout(buttons2, BoxLayout.X_AXIS));

        buttons1.add(b1);
        buttons1.add(Box.createRigidArea(HGAP15));
        buttons1.add(b2);

        buttons2.add(b3);
        buttons2.add(Box.createRigidArea(HGAP15));
        buttons2.add(b4);

        p.add(Box.createRigidArea(VGAP10));
        p.add(buttons1);
        p.add(Box.createRigidArea(VGAP15));
        p.add(buttons2);
        p.add(Box.createRigidArea(VGAP10));

        palette.getContentPane().add(p, BorderLayout.NORTH);

        // ************************************
        // * Create frame property checkboxes *
        // ************************************
        p = new JPanel() {
            Insets insets = new Insets(10, 15, 10, 5);

            public Insets getInsets() {
                return insets;
            }
        };
        p.setLayout(new GridLayout(1, 2));


        Box box = new Box(BoxLayout.Y_AXIS);
        windowResizable = new JCheckBox(getString("InternalFrameDemo.resizable_label"), true);
        windowIconifiable = new JCheckBox(getString("InternalFrameDemo.iconifiable_label"), true);

        box.add(Box.createGlue());
        box.add(windowResizable);
        box.add(windowIconifiable);
        box.add(Box.createGlue());
        p.add(box);

        box = new Box(BoxLayout.Y_AXIS);
        windowClosable = new JCheckBox(getString("InternalFrameDemo.closable_label"), true);
        windowMaximizable = new JCheckBox(getString("InternalFrameDemo.maximizable_label"), true);

        box.add(Box.createGlue());
        box.add(windowClosable);
        box.add(windowMaximizable);
        box.add(Box.createGlue());
        p.add(box);

        palette.getContentPane().add(p, BorderLayout.CENTER);

        // ************************************
        // *   Create Frame title textfield   *
        // ************************************
        p = new JPanel() {
            Insets insets = new Insets(0, 0, 10, 0);

            public Insets getInsets() {
                return insets;
            }
        };

        windowTitleField = new JTextField(getString("InternalFrameDemo.frame_label"));
        windowTitleLabel = new JLabel(getString("InternalFrameDemo.title_text_field_label"));

        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createRigidArea(HGAP5));
        p.add(windowTitleLabel, BorderLayout.WEST);
        p.add(Box.createRigidArea(HGAP5));
        p.add(windowTitleField, BorderLayout.CENTER);
        p.add(Box.createRigidArea(HGAP5));

        palette.getContentPane().add(p, BorderLayout.SOUTH);

        palette.show();

        return palette;
    }


    class ShowFrameAction extends AbstractAction {
        InternalFrameDemo demo;
        Icon icon;


        public ShowFrameAction(InternalFrameDemo demo, Icon icon) {
            this.demo = demo;
            this.icon = icon;
        }

        public void actionPerformed(ActionEvent e) {
            demo.createInternalFrame(icon,
                    getDemoFrameLayer(),
                    getFrameWidth(),
                    getFrameHeight());
        }
    }

    public int getFrameWidth() {
        return FRAME_WIDTH;
    }

    public int getFrameHeight() {
        return FRAME_HEIGHT;
    }

    public Integer getDemoFrameLayer() {
        return DEMO_FRAME_LAYER;
    }

    class ImageScroller extends JScrollPane {

        public ImageScroller(InternalFrameDemo demo, Icon icon, int layer, int count) {
            super();
            JPanel p = new JPanel();
            p.setBackground(Color.white);
            p.setLayout(new BorderLayout());

            p.add(new JLabel(icon), BorderLayout.CENTER);

            getViewport().add(p);
            getHorizontalScrollBar().setUnitIncrement(10);
            getVerticalScrollBar().setUnitIncrement(10);
        }

        public Dimension getMinimumSize() {
            return new Dimension(25, 25);
        }

    }

    void updateDragEnabled(boolean dragEnabled) {
        windowTitleField.setDragEnabled(dragEnabled);
    }

    private Properties bundle = null;

    private String getString(String key) {
        String value = null;
        try {
            if (bundle == null) {
                bundle = new Properties();
                bundle.load(getClass().getResourceAsStream("/org/jdesktop/swinghelper/layer/demo/resources/swingset.properties"));
            }
            value = bundle.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
        }
        if (value == null) {
            value = "Could not find resource: " + key + "  ";
        }
        return value;

    }

    private ImageIcon createImageIcon(String fileName, String str) {
        return new ImageIcon(getClass().getResource("/org/jdesktop/swinghelper/layer/demo/resources/images/" + fileName), str);
    }
}
