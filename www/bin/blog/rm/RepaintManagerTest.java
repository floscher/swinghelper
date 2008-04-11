import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RepaintManagerTest {

    private static void createGui() {
        
        JMenu optionsMenu = new JMenu("Options");
        
        final JCheckBoxMenuItem newRmItem = new JCheckBoxMenuItem("new RepaintManager");
        final JCheckBoxMenuItem doubleBufItem =  
                new JCheckBoxMenuItem("DoubleBuffering off and on");
        
        optionsMenu.add(newRmItem);
        newRmItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RepaintManager.setCurrentManager(new RepaintManager());
                
                newRmItem.setEnabled(false);
            }
        });
        
        optionsMenu.add(doubleBufItem);
        
        doubleBufItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RepaintManager rm = RepaintManager.currentManager(null);
                rm.setDoubleBufferingEnabled(false);
                rm.setDoubleBufferingEnabled(true);
                
                doubleBufItem.setEnabled(false);
            }
        });
        
        optionsMenu.addSeparator();
        JMenuItem clearItem = new JMenuItem("Clear textArea");
        optionsMenu.add(clearItem);
        
        JMenuBar bar = new JMenuBar();
        bar.add(optionsMenu);
        
        final JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setFont(textArea.getFont().deriveFont(20f));
        
        clearItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        
        final JFrame frame = new JFrame("RM test") {

            public void paint(Graphics g) {
                super.paint(g);
                String newLine = System.currentTimeMillis() + " JFrame.paint()\n";
                textArea.setText(textArea.getText() + newLine);
            }
        };
        
        frame.add(new JScrollPane(textArea));
        
        frame.setJMenuBar(bar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RepaintManagerTest.createGui();
            }
        });
    }
}
