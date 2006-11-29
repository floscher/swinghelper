package org.jdesktop.swinghelper.layer.demo;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class LafMenu extends JMenu {
    public LafMenu() {
        super("LookAndFeel");
        UIManager.LookAndFeelInfo[] lafs =
                UIManager.getInstalledLookAndFeels();
        for (final UIManager.LookAndFeelInfo laf : lafs) {
            JMenuItem item = new JMenuItem(laf.getName());
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        UIManager.setLookAndFeel(laf.getClassName());
                        SwingUtilities.updateComponentTreeUI(getTopLevelAncestor());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(item);
        }
    }
    
    static JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.add(new LafMenu());
        return bar;
    }
}
