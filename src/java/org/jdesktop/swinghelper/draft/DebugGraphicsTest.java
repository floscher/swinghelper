package org.jdesktop.swinghelper.draft;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Using DebugGraphics for Swing testing 
 */

public class DebugGraphicsTest {
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton button = new JButton("Hello");

                  // Try this options for debugging visible components
//                button.setDebugGraphicsOptions(DebugGraphics.LOG_OPTION);
//                RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
//                button.setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);

                frame.add(button);
                frame.setSize(200, 200);
                frame.setLocationRelativeTo(null);

                //you can also debug your components without making them visible:

                button.setSize(100, 50);
                BufferedImage image =
                        new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_RGB);
                final Graphics2D g2 = (Graphics2D) image.getGraphics();
                
                button.paint(new DebugGraphics(g2) {
                    public void setFont(Font aFont) {
                        System.out.println("Font is set, size: " + aFont.getSize2D());
                        super.setFont(aFont);
                    }
                });
                
                frame.setVisible(true);
            }
        });
    }
}
