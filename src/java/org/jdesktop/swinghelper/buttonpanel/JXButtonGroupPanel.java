package org.jdesktop.swinghelper.buttonpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;

/**
 * Container which makes it easy to manage button groups
 * it also supports correct focus and selection transfer
 * with help of arrow keys
 * 
 * Example:
 *
 * JXButtonGroupPanel panel = new JXButtonGroupPanel();  
 * panel.add(new JRadioButton("One"));
 * panel.add(new JRadioButton("Two"));
 * panel.add(new JRadioButton("Three"));  
 * 
 * @author Alexander Potochkin
 * https://swinghelper.dev.java.net/
 */
public class JXButtonGroupPanel extends JPanel {
    private JXButtonGroup group;
    private boolean isStopSelectionOnBoundary = true;

    public JXButtonGroupPanel() {
        this(true);
    }

    /**
     * Creates new instance of JXButtonGroupPanel,
     * if isExclusive is true only one button in the panel
     * will be allowed to be selected 
     *  
     * @param isExclusive 
     */
    public JXButtonGroupPanel(boolean isExclusive) {
        group = new JXButtonGroup(isExclusive);

        setFocusTraversalPolicyProvider(true);
        setFocusTraversalPolicy(new JXButtonPanelFocusTraversalPolicy());

        ActionListener actionHandler = new ActionHandler(this);

        registerKeyboardAction(actionHandler, ActionHandler.FORWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        registerKeyboardAction(actionHandler, ActionHandler.FORWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        registerKeyboardAction(actionHandler, ActionHandler.BACKWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        registerKeyboardAction(actionHandler, ActionHandler.BACKWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    protected void addImpl(Component c, Object constraints, int index) {
        if (c instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) c;
            group.add(button);
        }
        super.addImpl(c, constraints, index);
    }

    public void remove(int index) {
        Component c = getComponent(index);
        if (c instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) c;
            group.remove(button);
        }
        super.remove(index);
    }

    public void removeAll() {
        super.removeAll();
        group.removeAll();
    }

    public int getButtonCount() {
        return group.getButtonCount();
    }

    public AbstractButton getButton(int i) {
        return group.getButton(i);
    }

    public boolean isExclusive() {
        return group.isExclusive();
    }

    public boolean isStopSelectionOnBoundary() {
        return isStopSelectionOnBoundary;
    }

    /**
     * If stopSelectionOnBoundary is false 
     * than it will be possible to transfer focus and selection
     * with help of arrow button in cycle
     * that means that after the last button in the group
     * you'll get the first one 
     * @param stopSelectionOnBoundary
     */
    public void setStopSelectionOnBoundary(boolean stopSelectionOnBoundary) {
        isStopSelectionOnBoundary = stopSelectionOnBoundary;
    }

    /**
     * Clear selectoin of all buttons in the panel
     */
    public void clearSelection() {
        group.clearSelection();
    }

    private static class ActionHandler implements ActionListener {
        private final JXButtonGroupPanel panel;

        private static final String FORWARD = "moveSelectionForward";
        private static final String BACKWARD = "moveSelectionBackward";

        public ActionHandler(JXButtonGroupPanel panel) {
            this.panel = panel;
        }

        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            Component fo =
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            Component next = null;

            JXButtonPanelFocusTraversalPolicy ftp =
                    (JXButtonPanelFocusTraversalPolicy) panel.getFocusTraversalPolicy();
            ftp.isSkipUnselected = false;

            if (FORWARD.equals(actionCommand)) {
                if (!panel.isStopSelectionOnBoundary || fo != ftp.getLastComponent(panel)) {
                    next = ftp.getComponentAfter(panel, fo);
                    if (next == null) {
                        next = ftp.getFirstComponent(panel);
                    }
                }
            } else if (BACKWARD.equals(actionCommand)) {
                if (!panel.isStopSelectionOnBoundary || fo != ftp.getFirstComponent(panel)) {
                    next = ftp.getComponentBefore(panel, fo);
                    if (next == null) {
                        next = ftp.getLastComponent(panel);
                    }
                }
            } else {
                throw new AssertionError("Unexpected action command: " + actionCommand);
            }

            ftp.isSkipUnselected = true;

            if (fo instanceof AbstractButton) {
                AbstractButton b = (AbstractButton) fo;
                b.getModel().setPressed(false);                
            }
            if (next != null) {
                next.requestFocusInWindow();
                if (panel.group.contains(next)) {
                    AbstractButton b = (AbstractButton) next;
                    if (panel.group.getSelection() != null
                            && !b.isSelected()) {
                        b.setSelected(true);
                    }
                }
            }
        }
    }

    private class JXButtonPanelFocusTraversalPolicy extends LayoutFocusTraversalPolicy {
        private boolean isSkipUnselected = true;

        protected boolean accept(Component c) {
            if (isSkipUnselected && group.contains(c)) {
                AbstractButton b = (AbstractButton) c;
                if (group.getSelection() != null && !b.isSelected()) {
                    return false;
                }
            }
            return super.accept(c);
        }
    }
}
    