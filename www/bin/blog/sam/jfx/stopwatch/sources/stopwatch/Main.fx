/*
 * Main.fx
 */
package stopwatch;

import javafx.ext.swing.*;
import java.lang.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * @author Jasper
 */
var sw = StopwatchWidget{};
sw.frame = SwingFrame { content: sw.canvas width: 500 height: 500 };
//TODO: ShapedWindowHelper.makeShaped(sw.frame.getJFrame());
sw.frame.visible = true;
