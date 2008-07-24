/*
* : ShapedWindowHelper.java,v 1.1 2007/03/13 15:00:55 pottsj Exp $
*
* Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
* Santa Clara, California 95054, U.S.A. All rights reserved.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import java.awt.AWTEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Graphics2D;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import javax.swing.RepaintManager;

/**
 * ShapedWindowHelper
 *
 * @author Created by Jasper Potts (Feb 27, 2008)
 * @version 1.0
 */
public class ShapedWindowHelper {

    public static void makeShaped(final JFrame frame) {
        if ("Mac OS X".equals(System.getProperty("os.name"))) {
            frame.setUndecorated(true);
            frame.setBackground(new java.awt.Color(0, 0, 0, 0));
            frame.getRootPane().putClientProperty("Window.shadow", Boolean.TRUE);
            frame.addComponentListener(new ComponentAdapter() {
                /** Invoked when the component has been made visible. */
                @Override public void componentShown(ComponentEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            frame.getRootPane().putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());
                        }
                    });
                }
            });
        } else {
            //attempt to use the AWTUtilities via reflection so we can compile
            //and run on non 6u10 versions
            try {
                Class c = Class.forName("com.sun.awt.AWTUtilities");
                Method m = c.getMethod("setWindowOpaque", Window.class, boolean.class);
                m.invoke(null, frame, false);
                //RepaintManager.setCurrentManager(new BorkedRepaintManager());
            } catch (Exception ignored) {
                //didn't work, so punt
                return;
            }
            
            //add a handler to intercept AWT drag events. If they are not handled
            //by anything else, then use them to move the window around
            AWTEventListener handler = new AWTEventListener() {
                private int xOffset;
                private int yOffset;
                private boolean dragging = false;
                
                public void eventDispatched(AWTEvent event) {
                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent)event;
                        if (!e.isConsumed()) {
                            if (e.getID() == e.MOUSE_PRESSED && !dragging) {
                                //starting a drag
                                dragging = true;
                                //figure out the difference between the point of the
                                //window and the point of the mouse event. These
                                //will form our x and y offsets
                                Window win = getWindow(e);
                                Point winPoint = win.getLocationOnScreen();
                                Point mousePoint = e.getLocationOnScreen();
                                xOffset = winPoint.x - mousePoint.x; //causes xoffset to be negative
                                yOffset = winPoint.y - mousePoint.y; //causes yoffset to be negative
                            } else if (e.getID() == e.MOUSE_DRAGGED && dragging) {
                                Window win = getWindow(e);
                                Point mousePoint = e.getLocationOnScreen();
                                Point winPoint = win.getLocationOnScreen();
                                winPoint.x = mousePoint.x + xOffset;
                                winPoint.y = mousePoint.y + yOffset;
                                win.setLocation(winPoint);
                            } else if (e.getID() == e.MOUSE_RELEASED && dragging) {
                                dragging = false;
                            }
                        }
                    }
                }
                
                private Window getWindow(MouseEvent evt) {
                    Object src = evt.getSource();
                    if (src instanceof Component) {
                        return SwingUtilities.getWindowAncestor((Component)src);
                    } else {
                        return null;
                    }
                }
            };
            Toolkit.getDefaultToolkit().addAWTEventListener(handler, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }
    }

    public static void refreshShape(final JFrame frame) {
        if (frame!=null){
            if ("Mac OS X".equals(System.getProperty("os.name"))) {
                Graphics2D g2 = (Graphics2D)frame.getGraphics();
                Composite oldComposite = g2.getComposite();
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0,0,frame.getWidth(),frame.getHeight());
                g2.setComposite(oldComposite);
                frame.paint(g2);
                frame.getRootPane().putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());
            } else {
    //            Graphics2D g2 = (Graphics2D)f.getGraphics();
    //            Composite oldComposite = g2.getComposite();
    //            g2.setComposite(AlphaComposite.Clear);
    //            g2.fillRect(0,0,f.getWidth(),f.getHeight());
    //            g2.setComposite(oldComposite);
    //            f.paint(g2);
            }
        }
    }
}
