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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Sergey A. Malenkov
 */

def url = "http://javafx.com/samples/PathAnimation/webstart/PathAnimation.jar";
def file = "pathanimation/resources/car.png";
def image = Image {
  url: "jar:{url}!/{file}"
}

def count = 5;
def stage = Stage {
  title: "Colored Cars (JavaFX sample)"
  resizable: false
  scene: Scene {
    fill: Color.GRAY
    width: 600
    height: image.height * count
    content: for (i in [1..count]) ImageView {
      translateX: -image.width
      translateY: -image.height * (1 - i)
      image: image
      effect: ColorAdjust {
        hue: 1.0 - 2.0 * i / count
      }
    }
  }
}
for (node in stage.scene.content) Timeline {
  repeatCount: Timeline.INDEFINITE
  keyFrames: [
    at (0s) {node.translateX => -image.width}
    KeyFrame {
      time: Math.random() * 200ms + 2s
      values: node.translateX => stage.scene.width
    }
  ]
}.play()
