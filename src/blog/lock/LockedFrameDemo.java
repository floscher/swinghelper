package lock;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class LockedFrameDemo extends JFrame {
    private LockingGlassPane lockingGlassPane;

    public LockedFrameDemo() {
        super("Locked frame demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        final JMenuItem showItem = new JMenuItem("Show GlassPane");
        showItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        showItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getGlassPane().setVisible(true);
            }
        });

        menu.add(showItem);
        menu.addSeparator();

        bar.add(menu);
        setJMenuBar(bar);
        
        // GlassPane related code start
        lockingGlassPane = new LockingGlassPane();
        lockingGlassPane.setLayout(new GridBagLayout());
        final JButton unlockButton = new JButton("Cancel");
        unlockButton.setCursor(Cursor.getDefaultCursor());
        lockingGlassPane.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                unlockButton.requestFocusInWindow();
            }

            public void focusLost(FocusEvent e) {

            }
        });
        unlockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lockingGlassPane.setVisible(false);
            }
        });
        lockingGlassPane.add(unlockButton);        

        setGlassPane(lockingGlassPane);        
        // GlassPane related code end

        add(createPanel());
        setSize(250, 250);
        setLocationRelativeTo(null);
    }

    private JComponent createPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField(15));
        panel.add(new JSlider());
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField(15));
        final JButton button = new JButton("Show a dialog");
        button.setMnemonic('W');
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(button.getTopLevelAncestor(), "This button has a mnemonic (alt + W) " +
                        "which is also locked when the LockingGlassPane is visible", "It works!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(button);
        return panel;
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LockedFrameDemo().setVisible(true);
            }
        });
    }
}
