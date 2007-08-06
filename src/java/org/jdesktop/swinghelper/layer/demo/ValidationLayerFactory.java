package org.jdesktop.swinghelper.layer.demo;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Alexander Potochkin
 *         
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/
 * 
 * @see TextValidationDemo
 */
public class ValidationLayerFactory {
    
    // shared translucent painter
    private static final TranslucentPainter<JTextComponent> TRANSLUCENT_PAINTER
            = new TranslucentPainter<JTextComponent>();

    public static JXLayer<JTextComponent> createValidationLayer(JTextComponent c) {
        return new JXLayer<JTextComponent>(c, TRANSLUCENT_PAINTER);
    }
    
    static class TranslucentPainter<V extends JTextComponent> extends DefaultPainter<V> {
        public void paint(Graphics2D g2, JXLayer<V> l) {
            // paints the layer as is
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

            g2.setColor(view.getText().toLowerCase().equals("jxlayer") ? 
                    Color.GREEN : Color.RED);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
            g2.fillRect(0, 0, l.getWidth(), l.getHeight());
        }
    }
    

    // shared icon painter
    private static final IconPainter<JTextComponent> ICON_PAINTER
            = new IconPainter<JTextComponent>();

    public static JXLayer<JTextComponent> createIconLayer(JTextComponent c) {

        final JXLayer<JTextComponent> layer = new JXLayer<JTextComponent>(c, ICON_PAINTER);

        // set necessary insets for the layer
        layer.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 3));

        // layer's border area should be repainted when textComponent is updated
        layer.getView().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                layer.repaint();
            }

            public void removeUpdate(DocumentEvent e) {
                layer.repaint();
            }

            public void changedUpdate(DocumentEvent e) {
                layer.repaint();
            }
        });
        return layer;
    }

    static class IconPainter<V extends JTextComponent> extends DefaultPainter<V> {

        // The red icon to be shown at the layer's corner
        private final static BufferedImage INVALID_ICON;
        static {
            int width = 7;
            int height = 8;
            INVALID_ICON = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) INVALID_ICON.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(Color.RED);
            g2.fillRect(0, 0, width, height);
            g2.setColor(Color.WHITE);
            g2.drawLine(0, 0, width, height);
            g2.drawLine(0, height, width, 0);
            g2.dispose();

        }

        public void paint(Graphics2D g2, JXLayer<V> l) {
            super.paint(g2, l);

            // There is no need to take insets into account for this painter
            if (!l.getView().getText().toLowerCase().equals("jxlayer")) {
                g2.drawImage(INVALID_ICON, l.getWidth() - INVALID_ICON.getWidth() - 1, 0, null);
            }
        }
    }
}
