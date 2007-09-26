package nohacks;

import org.jdesktop.swinghelper.layer.JXLayer;
import org.jdesktop.swinghelper.layer.painter.DefaultPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Test1 extends JFrame {

    private JXLayer<JButton> layer;

    public Test1() {
        super("Overlapping test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(null) {
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };

        MoveAction moveRight = new MoveAction("MoveRight");
        MoveAction moveLeft = new MoveAction("MoveLeft");
        MoveAction moveUp = new MoveAction("MoveUp");
        MoveAction moveDown = new MoveAction("MoveDown");

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Move Button");
        bar.add(menu);
        JMenuItem rightItem = new JMenuItem(moveRight);
        rightItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true));
        menu.add(rightItem);
        JMenuItem leftItem = new JMenuItem(moveLeft);
        leftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true));
        menu.add(leftItem);
        JMenuItem upItem = new JMenuItem(moveUp);
        upItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true));
        menu.add(upItem);
        JMenuItem downItem = new JMenuItem(moveDown);
        downItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true));
        menu.add(downItem);
        
        setJMenuBar(bar);

        TranslucentComponent tc = new TranslucentComponent();
        tc.setBounds(10, 0, 220, 220);
        panel.add(tc);

        layer = createLayer();
        
        layer.setLocation(70, 80);
        panel.add(layer);

        add(panel);

        setSize(250, 270);
        setLocationRelativeTo(null);
    }

    public static JXLayer<JButton> createLayer() {
        JXLayer<JButton> l = new JXLayer<JButton>(new JButton("JXLayer"));
        l.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        l.setSize(l.getPreferredSize());

        l.setPainter(new DefaultPainter<JButton>() {

            public void paint(Graphics2D g2, JXLayer<JButton> l) {
                super.paint(g2, l);
                g2.setColor(Color.RED);
                int size = 10;
                g2.fillRect(0, 0, size, size);
                g2.fillRect(0, l.getHeight() - size, size, size);
                g2.fillRect(l.getWidth() - size, 0, size, size);
                g2.fillRect(l.getWidth() - size, l.getHeight() - size, size, size);
            }
        });
        return l;
    }


    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Test1().setVisible(true);
            }
        });
    }

    class MoveAction extends AbstractAction {

        public MoveAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            int i = 3;
            Object name = getValue(Action.NAME);
            if ("MoveRight".equals(name)) {
                layer.setLocation(layer.getX() + i, layer.getY());
            } else if ("MoveLeft".equals(name)) {
                layer.setLocation(layer.getX() - i, layer.getY());
            } else if ("MoveUp".equals(name)) {
                layer.setLocation(layer.getX(), layer.getY() - i);
            } else if ("MoveDown".equals(name)) {
                layer.setLocation(layer.getX(), layer.getY() + i);
            }  
        }
    }
}

class TranslucentComponent extends JComponent {

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = 32;
        g2.setStroke(new BasicStroke(size));
        g2.drawOval(size, size, getWidth() - 2 * size, getHeight() - 2 * size);
        g2.dispose();
    }
}
