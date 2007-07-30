package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;
import org.jdesktop.swinghelper.layer.demo.util.LafMenu;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.BadLocationException;
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

        l1 = new JXLayer<JTextComponent>(
                new JTextField(10), new AlphaPainter());
        layerBox.add(Box.createGlue());
        layerBox.add(l1);

        l2 = new JXLayer<JTextComponent>(
                new JTextField("Decoration", 10), new LockPainter());
        layerBox.add(Box.createGlue());
        layerBox.add(l2);

        l3 = new JXLayer<JTextComponent>(
                new JTextField("Modified selection", 10), new SelectionPainter());
        layerBox.add(Box.createGlue());
        layerBox.add(l3);

        l4 = new JXLayer<JTextComponent>(
                new JTextField("Hidden digits", 10), new SecretPainter());
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

    class AlphaPainter extends DefaultPainter<JTextComponent> {
        public void paint(Graphics2D g2, JXLayer<JTextComponent> l) {
            super.paint(g2, l);

            JTextComponent view = l.getView();
            // To prevent painting on view's border
            Insets insets = view.getInsets();
            g2.clip(new Rectangle(insets.left, insets.top,
                    view.getWidth() - insets.left - insets.right,
                    view.getHeight() - insets.top - insets.bottom));

            g2.setColor(view.getText().toLowerCase().equals("hello") ? 
                    Color.GREEN : Color.RED);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
            g2.fillRect(0, 0, l.getWidth(), l.getHeight());
        }
    }
    
    class LockPainter extends DefaultPainter<JTextComponent> {
        public void paint(Graphics2D g2, JXLayer<JTextComponent> l) {
            super.paint(g2, l);
            
            if (!l.getView().isEditable()) {
                g2.setComposite((AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f)));
                g2.setColor(Color.ORANGE);
                g2.fillRect(1, l.getHeight() - 7, 5, 5);
                g2.setColor(Color.BLACK);
                g2.drawRect(1, l.getHeight() - 7, 5, 5);
                g2.drawArc(2, l.getHeight() - 9, 4, 4, 0, 180);
                g2.fillRect(3, l.getHeight() - 5, 2, 2);
            }
        }
    }
    
    class SelectionPainter extends DefaultPainter<JTextComponent> {
        public void paint(Graphics2D g2, JXLayer<JTextComponent> l) {
            super.paint(g2, l);

            JTextComponent view = l.getView();
            if (view.isFocusOwner()) {
                int start = view.getSelectionStart();
                int end = view.getSelectionEnd();
                if (start != end) {
                    g2.setColor(Color.RED);
                    try {
                        Rectangle startRect = view.modelToView(start);
                        Rectangle endRect = view.modelToView(end);
                        g2.drawLine(startRect.x, startRect.y, endRect.x, endRect.y);
                    } catch (BadLocationException e) {
                        // can't happen
                    }
                }
            }
        }
    }
    
    class SecretPainter extends DefaultPainter<JTextComponent> {
        public void paint(Graphics2D g2, JXLayer<JTextComponent> l) {
            super.paint(g2, l);

            JTextComponent view = l.getView();
            // To prevent painting on view's border
            Insets insets = view.getInsets();
            g2.clip(new Rectangle(insets.left, insets.top,
                    view.getWidth() - insets.left - insets.right,
                    view.getHeight() - insets.top - insets.bottom));

            String text = view.getText();
            int selectionStart = view.getSelectionStart();
            int selectionEnd = view.getSelectionEnd();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (Character.isDigit(c)) {
                    try {
                        Rectangle start = view.modelToView(i);
                        int stringWidth = view.getFontMetrics(view.getFont()).stringWidth(String.valueOf(c));
                        if (!view.isFocusOwner() || i < selectionStart || i >= selectionEnd) {
                            g2.setColor(view.getBackground());
                            g2.fillRect(start.x, start.y, stringWidth, start.height);
                            g2.setColor(view.getForeground());
                        } else {
                            Rectangle next = view.modelToView(i + 1);
                            g2.setColor(view.getSelectionColor());
                            g2.fillRect(start.x, start.y, next.x - start.x, start.height);
                            g2.setColor(UIManager.getColor("TextField.selectionForeground"));
                        }
                        int min = Math.min(stringWidth - 1, start.height);
                        int delta = (Math.max(stringWidth - 1, start.height) - min) / 2;
                        g2.fillOval(start.x + 1, start.y + delta, min, min);
                    } catch (BadLocationException e) {
                        //can't happen
                    }
                }
            }
            // caret can be damaged by previous painting
            view.getCaret().paint(g2);
        }
    }
}
