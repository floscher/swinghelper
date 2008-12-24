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

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * @author Sergey A. Malenkov
 */

def colors = [ Color.rgb(0xEE,0,0)   Color.rgb(0xEE,0xEE,0)   Color.GREEN ];

class DragDropNode extends CustomNode {
  var node: Node;
  override function create() {
    Group {
      var x: Number;
      var y: Number;
      onMousePressed: function(event) {
        x = translateX;
        y = translateY;
        toFront();
        effect = Glow {
          level: 0.5
        }
      }
      onMouseDragged: function(event) {
        translateX = x + event.dragX;
        translateY = y + event.dragY;
      }
      onMouseReleased: function(event) {
        effect = null
      }
      blocksMouse: true
      content: bind node
    }
  }
}

Stage {
  title: "Drag and Drop 4 (JavaFX sample)"
  scene: Scene {
    fill: Color.BLACK
    width: 400
    height: 400
    content: Group {
      translateX: 200
      translateY: -40
      var offset: Number;
      content: for (color in colors)
        DragDropNode {
          node: Circle {
            fill: color
            centerY: offset += 110
            radius: 50
          }
        }
    }
  }
}
