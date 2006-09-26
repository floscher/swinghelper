package org.jdesktop.swinghelper.debug;

import javax.swing.*;
import java.lang.ref.WeakReference;

/**
 * <p>This class is used to detect Event Dispatch Thread rule violations<br>
 * See <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How to Use Threads</a>
 * for more info</p>
 * <p/>
 * <p>This is a modification of original idea of Scott Delap<br>
 * Initial version of ThreadCheckingRepaintManager can be found here<br>
 * <a href="http://www.clientjava.com/blog/2004/08/20/1093059428000.html">Easily Find Swing Threading Mistakes</a>
 * </p>
 *
 * @author Scott Delap
 *         <p/>
 *         some improvements from Alexander Potochkin
 */
public class CheckThreadViolationRepaintManager extends RepaintManager {
    // it is recommended to pass the complete check  
    private boolean completeCheck = true;
    private WeakReference<JComponent> lastComponent;

    public CheckThreadViolationRepaintManager(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    public CheckThreadViolationRepaintManager() {
        this(true);
    }

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
            boolean imageUpdate = false;
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement st : stackTrace) {
                if (repaint && st.getClassName().startsWith("javax.swing.")) {
                    fromSwing = true;
                }
                if (repaint && "imageUpdate".equals(st.getMethodName())) {
                    imageUpdate = true;
                }
                if ("repaint".equals(st.getMethodName())) {
                    repaint = true;
                }
            }
            if (imageUpdate) {
                //assuming it is java.awt.image.ImageObserver.imageUpdate(...) 
                //image was asynchronously updated, that's ok 
                return;
            }
            if (repaint && !fromSwing) {
                //no problems here, since repaint() is thread safe
                return;
            }
            //ignore the last processed component
            if (lastComponent != null && c == lastComponent.get()) {
                return;
            }
            lastComponent = new WeakReference<JComponent>(c);
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
        System.out.println("Valid code passed...");
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

    //this test must pass
    static void imageUpdateTest() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JEditorPane editor = new JEditorPane();
        frame.setContentPane(editor);
        editor.setContentType("text/html");
        //it works with no valid image as well 
        editor.setText("<html><img src=\"file:\\lala.png\"></html>");
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}