package org.jdesktop.swinghelper.buttonpanel;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * @author Alexander Potochkin
 * https://swinghelper.dev.java.net/
 */
class JXButtonGroup extends ButtonGroup {
    private List<AbstractButton> buttonList = new ArrayList<AbstractButton>();
    private ButtonModel selection;
    private boolean isExclusive;

    public JXButtonGroup() {
        this(true);
    }

    public JXButtonGroup(boolean exclusive) {
        isExclusive = exclusive;
    }

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setSelected(ButtonModel m, boolean b) {
        if (m == null || m.isSelected() == b) {
            return;
        }
        if (isExclusive) {
            if (b && m != selection) {
                ButtonModel oldSelection = selection;
                selection = m;
                if (oldSelection != null) {
                    oldSelection.setSelected(false);
                }
                m.setSelected(true);
            }
        }
    }

    public void add(AbstractButton b) {
        if (b == null) {
            return;
        }
        buttonList.add(b);

        if (isExclusive && b.isSelected()) {
            if (selection == null) {
                selection = b.getModel();
            } else {
                b.setSelected(false);
            }
        }

        b.getModel().setGroup(this);
    }

    public void remove(AbstractButton b) {
        if (b == null) {
            return;
        }
        buttonList.remove(b);
        if (isExclusive && b.getModel() == selection) {
            selection = null;
        }
        b.getModel().setGroup(null);
    }

    public void removeAll() {
        buttonList.clear();
        selection = null;
    }

    public void clearSelection() {
        if (isExclusive) {
            if (selection != null) {
                ButtonModel oldSelection = selection;
                selection = null;
                oldSelection.setSelected(false);
            }
        } else {
            for (AbstractButton button : buttonList) {
                button.getModel().setGroup(null);
                button.setSelected(false);
                button.getModel().setGroup(this);
            }
        }
    }

    public Enumeration<AbstractButton> getElements() {
        return Collections.enumeration(buttonList);
    }

    public boolean contains(Component c) {
        return buttonList.contains(c);
    }

    public ButtonModel getSelection() {
        return isExclusive ? selection : null;
    }

    public boolean isSelected(ButtonModel m) {
        return isExclusive ? (m == selection) : !m.isSelected();
    }

    public int getButtonCount() {
        return buttonList.size();
    }

    public AbstractButton getButton(int i) {
        return buttonList.get(i);
    }
}
