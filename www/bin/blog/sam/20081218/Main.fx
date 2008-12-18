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

import java.lang.Math;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Sergey A. Malenkov
 */

def colors = [ Color.CYAN   Color.GREEN   Color.YELLOW ];
var stage: Stage = Stage {
  title: "Clock (JavaFX sample)"
  style: StageStyle.TRANSPARENT
  scene: Scene {
    fill: null
    width: 320
    height: 320
    content: Clock {
      paused: false
      radius: bind Math.min(stage.scene.width / 2, stage.scene.height / 2)
      stroke: colors[sizeof colors / 2]
      fill: LinearGradient {
        def factor = sizeof colors - 1.0;
        stops: for (color in colors) Stop {
          color: color
          offset: indexof color / factor
        }
      }
      onMouseDragged: function(event) {
        stage.x += event.dragX;
        stage.y += event.dragY;
      }
    }
  }
}
