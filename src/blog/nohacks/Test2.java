package nohacks;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.*;

public class Test2 {

    private static void createGui() {
        final JFrame frame = new JFrame("ScrollPane Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new FlowLayout());
        JPanel panel = new JPanel();
        JXLayer<JButton> layer = Test1.createLayer();
        panel.add(layer);
        JScrollPane scroll = new JScrollPane(panel);
        panel.setPreferredSize(new Dimension(500, 200));
        scroll.setPreferredSize(new Dimension(200, 100));
        frame.add(scroll);

        frame.setSize(220, 200);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        layer.scrollRectToVisible(new Rectangle(layer.getWidth(), layer.getHeight()));
        
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Test2.createGui();
            }
        });
    }
}
