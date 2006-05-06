package org.jdesktop.swinghelper.draft;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.awt.event.*;

/**
 * Using Proxy interfaces for logging
 */

public class LoggerProxy {

    @SuppressWarnings("unchecked")
    public static <T, C extends T> C getProxy(final T object, final Class<T> theInterface) {
        ClassLoader classLoader = object.getClass().getClassLoader();
        return (C) Proxy.newProxyInstance
                (classLoader, new Class<?>[] {theInterface}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        
                        // Logging is here
                        System.out.println("method = " + method);
                        for (Object arg : args) {
                            System.out.println("arg = " + arg);
                        }

                        return method.invoke(object, args);
                    }
                });
    }

    public static void main(String[] args) {
        WindowAdapter adapter = new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                System.out.println("WindowAdapter.windowActivated");
            }

            public void windowStateChanged(WindowEvent e) {
                System.out.println("WindowAdapter.windowStateChanged");
            }
        };
        WindowListener windowListener = 
                LoggerProxy.getProxy(adapter, WindowListener.class);
        windowListener.windowActivated(null);
        
        WindowStateListener windowStateListener = 
                LoggerProxy.getProxy(adapter, WindowStateListener.class);
        windowStateListener.windowStateChanged(null);
    }
}
