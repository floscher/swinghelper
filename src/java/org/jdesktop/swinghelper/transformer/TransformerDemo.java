package org.jdesktop.swinghelper.transformer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TransformerDemo extends JFrame implements ChangeListener {

    private List<JXTransformer> transformers = new ArrayList<JXTransformer>();
    private JSlider rotationSlider;
    private JSlider scalingSlider;
    private JSlider shearingSlider;

    public TransformerDemo() {
        super("Transformer demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();
        JMenu lafMenu = new JMenu("LaF");

        JMenuItem winLaf = new JMenuItem("Windows LaF");
        lafMenu.add(winLaf);
        winLaf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setLaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        });
        JMenuItem motifLaf = new JMenuItem("Motif LaF");
        lafMenu.add(motifLaf);
        motifLaf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setLaf("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            }
        });
        bar.add(lafMenu);
        JMenuItem metalLaf = new JMenuItem("Metal LaF");
        lafMenu.add(metalLaf);
        metalLaf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setLaf("javax.swing.plaf.metal.MetalLookAndFeel");
            }
        });

        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem item = new JMenuItem("Reset sliders", KeyEvent.VK_R);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotationSlider.setValue(0);
                scalingSlider.setValue(100);
                shearingSlider.setValue(0);
            }
        });
        settingsMenu.add(item);
        bar.add(settingsMenu);
        setJMenuBar(bar);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createDemoPanel());
        panel.add(createStressTestPanel(), BorderLayout.EAST);
        add(new JScrollPane(panel));
        add(new JScrollPane(createToolPanel()), BorderLayout.SOUTH);
        pack();
    }

    private void setLaf(String laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (JXTransformer t : transformers) {
            t.revalidate();
            t.repaint();
        }
    }

    private JPanel createStressTestPanel() {
        JPanel panel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Stress test (with tooltips)");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(titledBorder);
        JButton lowerButton = new JButton("Button");
        lowerButton.setLayout(new FlowLayout());
        lowerButton.setToolTipText("Lower button");
        JButton middleButton = new JButton();
        middleButton.setToolTipText("Middle button");
        middleButton.setLayout(new FlowLayout());
        lowerButton.add(middleButton);
        JButton upperButton = new JButton("Upper button");
        upperButton.setToolTipText("Upper button");
        middleButton.add(upperButton);
        panel.add(createTransformer(lowerButton));
        return panel;
    }

    private JPanel createDemoPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Try three sliders below !");
        Font titleFont = titledBorder.getTitleFont();
        titledBorder.setTitleFont(titleFont.deriveFont(titleFont.getSize2D() + 10));
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        buttonPanel.setBorder(titledBorder);
        JButton b = new JButton("JButton");
        b.setPreferredSize(new Dimension(100,50));
        buttonPanel.add(createTransformer(b));

        Vector<String> v = new Vector<String>();
        v.add("One");
        v.add("Two");
        v.add("Three");
        JList list = new JList(v);
        buttonPanel.add(createTransformer(list));
        
        buttonPanel.add(createTransformer(new JCheckBox("JCheckBox")));
        
        JSlider slider = new JSlider(0, 100);
        slider.setLabelTable(slider.createStandardLabels(25, 0));
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        buttonPanel.add(createTransformer(slider));

        buttonPanel.add(createTransformer(new JRadioButton("JRadioButton")));
        final JLabel label = new JLabel("JLabel");
        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Font font = label.getFont();
                label.setFont(font.deriveFont(font.getSize2D() + 10));
            }

            public void mouseExited(MouseEvent e) {
                Font font = label.getFont();
                label.setFont(font.deriveFont(font.getSize2D() - 10));
            }
        });
        buttonPanel.add(createTransformer(label));

        return buttonPanel;
    }

    private JXTransformer createTransformer(JComponent c) {
        JXTransformer t = new JXTransformer(c);
        transformers.add(t);
        return t;
    }

    private JPanel createToolPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 0));
        rotationSlider = new JSlider(-180, 180, 0);
        rotationSlider.setLabelTable(rotationSlider.createStandardLabels(90, -180));
        rotationSlider.setPaintLabels(true);
        rotationSlider.setPaintTicks(true);
        rotationSlider.setMajorTickSpacing(45);
        rotationSlider.addChangeListener(this);
        rotationSlider.setBorder(BorderFactory.createTitledBorder("Rotate"));
        panel.add(rotationSlider);

        shearingSlider = new JSlider(-10, 10, 0);
        shearingSlider.setLabelTable(shearingSlider.createStandardLabels(5, -10));
        shearingSlider.setPaintLabels(true);
        shearingSlider.setPaintTicks(true);
        shearingSlider.setMajorTickSpacing(2);
        shearingSlider.addChangeListener(this);
        shearingSlider.setBorder(BorderFactory.createTitledBorder("Shear"));
        panel.add(shearingSlider);
        
        scalingSlider = new JSlider(50, 150, 100);
        scalingSlider.setLabelTable(scalingSlider.createStandardLabels(25, 50));
        scalingSlider.setPaintLabels(true);
        scalingSlider.setPaintTicks(true);
        scalingSlider.setMajorTickSpacing(10);
        scalingSlider.addChangeListener(this);
        scalingSlider.setBorder(BorderFactory.createTitledBorder("Scale"));
        panel.add(scalingSlider);
        return panel;
    }

    public void stateChanged(ChangeEvent e) {
        AffineTransform at = new AffineTransform();
        at.rotate(rotationSlider.getValue() * Math.PI / 180);
        double scale = scalingSlider.getValue() / 100.0;
        at.scale(scale, scale);
        double shear = shearingSlider.getValue() / 10.0;
        at.shear(shear, 0);
        for (JXTransformer t : transformers) {
            t.setTransform(at);
        }
    }

    public static void main(String[] args) {
        TransformerDemo demo = new TransformerDemo();
        demo.setSize(800, 600);
        demo.setVisible(true);
    }
}