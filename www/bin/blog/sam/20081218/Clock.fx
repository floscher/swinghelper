/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

import java.util.Calendar;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * @author Sergey A. Malenkov
 */

public class Clock extends CustomNode {
  var calendar = getCalendar();
  def timer = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: KeyFrame {
      time: 1s
      action: function() {
        calendar = getCalendar()
      }
    }
  }
  public var fill: Paint;
  public var stroke: Paint;
  public var thick: Number;
  public var radius: Number on replace {
    if (thick <= 0.0) thick = radius * 0.03
  }
  public var paused = true on replace {
    if (not paused) {
      calendar = getCalendar();
      timer.play()
    } else {
      timer.stop()
    }
  }
  override function create() {
    Group {
      translateX: bind radius
      translateY: bind radius
      content: [
        for (i in [1 .. 12]) Rectangle {
          x:         bind - thick * 0.5
          y:         bind - radius
          width:     bind thick
          height:    bind radius * 0.2
          arcWidth:  bind thick
          arcHeight: bind thick
          stroke:    bind stroke
          fill:      bind fill
          transforms: Rotate {
            angle: 30 * i
          }
        }
        Rectangle {
          x:         bind - thick * 1.5
          y:         bind - radius * 0.6
          width:     bind thick * 3
          height:    bind radius * 0.8
          arcWidth:  bind thick * 3
          arcHeight: bind thick * 3
          stroke:    bind stroke
          fill:      bind fill
          transforms: Rotate {
            angle: bind 30 * calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) / 2
          }
        }
        Rectangle {
          x:         bind - thick
          y:         bind - radius * 0.8
          width:     bind thick * 2
          height:    bind radius
          arcWidth:  bind thick * 2
          arcHeight: bind thick * 2
          stroke:    bind stroke
          fill:      bind fill
          transforms: Rotate {
            angle: bind 6 * calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) / 10
          }
        }
        Rectangle {
          x:         bind - thick * 0.5
          y:         bind - radius
          width:     bind thick
          height:    bind radius * 1.2
          arcWidth:  bind thick
          arcHeight: bind thick
          stroke:    bind stroke
          fill:      bind fill
          transforms: Rotate {
            angle: bind 6 * calendar.get(Calendar.SECOND)
          }
        }
        Circle {
          radius: bind thick * 2
          stroke: bind stroke
          fill:   bind fill
        }
      ]
    }
  }
  function getCalendar() {
    def calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar
  }
}
