package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.BadLocationException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;

/**
 * @author Alexander Potochkin
 *         
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/
 * 
 * @see TextComponentPainterDemo
 */
public class TextComponentLayerFactory {
    private static final AlphaPainter<JTextComponent> sharedAlphaPainter 
            = new AlphaPainter<JTextComponent>();
    private static final LockPainter<JTextComponent> sharedLockPainter 
            = new LockPainter<JTextComponent>();
    private static final SelectionPainter<JTextComponent> sharedSelectionPainter 
            = new SelectionPainter<JTextComponent>();
    private static final SecretPainter<JTextComponent> sharedSecretPainter 
            = new SecretPainter<JTextComponent>();
    
    // Factory methods
    
    public static JXLayer<JTextComponent> createAlphaLayer(JTextComponent c) {
        return new JXLayer<JTextComponent>(c, sharedAlphaPainter);
    }
    
    public static JXLayer<JTextComponent> createLockLayer(JTextComponent c) {
        final JXLayer<JTextComponent> layer 
                = new JXLayer<JTextComponent>(c, sharedLockPainter);
        // set necessary insets for the layer 
        layer.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        // layer's border area should be repainted when "editable" property is changed 
        layer.getView().addPropertyChangeListener("editable", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                layer.repaint();
            }
        });
        return layer;
    }
    
    public static JXLayer<JTextComponent> createSelectionLayer(JTextComponent c) {
        return new JXLayer<JTextComponent>(c, sharedSelectionPainter);
    }
    
    public static JXLayer<JTextComponent> createSecretLayer(JTextComponent c) {
        return new JXLayer<JTextComponent>(c, sharedSecretPainter);
    }
    
    // Painters
    
    static class AlphaPainter<V extends JTextComponent> extends DefaultPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            super.paint(g2, l);

            // to be in sync with the view if the layer has a border
            Insets layerInsets = l.getInsets();
            g2.translate(layerInsets.left, layerInsets.top);
            
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
    
    static class LockPainter<V extends JTextComponent> extends DefaultPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            super.paint(g2, l);

            // There is no need to take insets into account for this painter

            if (!l.getView().isEditable()) {
                g2.setColor(Color.ORANGE);
                g2.fillRect(2, l.getHeight() - 7, 5, 5);
                g2.setColor(Color.BLACK);
                g2.drawRect(2, l.getHeight() - 7, 5, 5);
                g2.drawArc(3, l.getHeight() - 9, 3, 4, 0, 180);
                g2.fillRect(4, l.getHeight() - 5, 2, 2);
            }
        }
    }
    
    static class SelectionPainter<V extends JTextComponent> extends DefaultPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            super.paint(g2, l);

            // to be in sync with the view if the layer has a border
            Insets layerInsets = l.getInsets();
            g2.translate(layerInsets.left, layerInsets.top);

            JTextComponent view = l.getView();
            if (view.isFocusOwner()) {
                int start = view.getSelectionStart();
                int end = view.getSelectionEnd();
                if (start != end) {
                    g2.setColor(Color.RED);
                    try {
                        Rectangle startRect = view.modelToView(start);
                        Rectangle endRect = view.modelToView(end);
                        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                        g2.drawLine(startRect.x, startRect.y + 1, endRect.x, endRect.y + 1);
                    } catch (BadLocationException e) {
                        // can't happen
                    }
                }
            }
        }
    }
    
    static class SecretPainter<V extends JTextComponent> extends DefaultPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            super.paint(g2, l);

            // to be in sync with the view if the layer has a border
            Insets layerInsets = l.getInsets();
            g2.translate(layerInsets.left, layerInsets.top);

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
