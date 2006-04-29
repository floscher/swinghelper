package org.swinghelper.debug;

import javax.swing.*;

/**
 * @author Scott Delap
 */
public class CheckThreadViolationRepaintManager extends RepaintManager {
    // it is recommended to pass the complete check  
    private boolean completeCheck = true;

    public boolean isCompleteCheck() {
        return completeCheck;
    }

    public void setCompleteCheck(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    public synchronized void addInvalidComponent(JComponent component) {
        checkThreadViolations(component);
        super.addInvalidComponent(component);
    }

    public void addDirtyRegion(JComponent component, int x, int y, int w, int h) {
        checkThreadViolations(component);
        super.addDirtyRegion(component, x, y, w, h);
    }

    private void checkThreadViolations(JComponent c) {
        if (!SwingUtilities.isEventDispatchThread() && (completeCheck || c.isShowing())) {
            boolean repaint = false;
            boolean fromSwing = false;
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement st : stackTrace) {
                if (repaint && st.getClassName().startsWith("javax.swing.")) {
                    fromSwing = true;
                }
                if ("repaint".equals(st.getMethodName())) {
                    repaint = true;
                }
            }
            if (repaint && !fromSwing) {
                //no problems here, since repaint() is thread safe
                return;
            }
            System.out.println();       
            System.out.println("EDT violation detected");
            System.out.println(c);
            for (StackTraceElement st : stackTrace) {
                System.out.println("\tat " + st);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //set CheckThreadViolationRepaintManager 
        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        //Valid code  
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                test();
            }
        });
        //Invalide code (stack trace expected) 
        test();
    }

    static void test() {
        JFrame frame = new JFrame("Am I on EDT?");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JButton("JButton"));
        frame.pack();
        frame.setVisible(true);
        frame.dispose();
    }
}