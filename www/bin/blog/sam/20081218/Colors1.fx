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

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author Sergey A. Malenkov
 */

def size = 20; // the color box size
def space = 2; // the space between boxes

def width  = 10;
def height = 14;

def colors = [
  "aliceblue",       "antiquewhite",     "aqua",                 "aquamarine",
  "azure",           "beige",            "bisque",               "black",
  "blanchedalmond",  "blue",             "blueviolet",           "brown",
  "burlywood",       "cadetblue",        "chartreuse",           "chocolate",
  "coral",           "cornflowerblue",   "cornsilk",             "crimson",
  "cyan",            "darkblue",         "darkcyan",             "darkgoldenrod",
  "darkgreen",       "darkgrey",         "darkkhaki",            "darkmagenta",
  "darkolivegreen",  "darkorange",       "darkorchid",           "darkred",
  "darksalmon",      "darkseagreen",     "darkslateblue",        "darkslategrey",
  "darkturquoise",   "darkviolet",       "deeppink",             "deepskyblue",
  "dimgrey",         "dodgerblue",       "firebrick",            "floralwhite",
  "forestgreen",     "fuchsia",          "gainsboro",            "ghostwhite",
  "gold",            "goldenrod",        "green",                "greenyellow",
  "grey",            "honeydew",         "hotpink",              "indianred",
  "indigo",          "ivory",            "khaki",                "lavender",
  "lavenderblush",   "lawngreen",        "lemonchiffon",         "lightblue",
  "lightcoral",      "lightcyan",        "lightgoldenrodyellow", "lightgreen",
  "lightgrey",       "lightpink",        "lightsalmon",          "lightseagreen",
  "lightskyblue",    "lightslategrey",   "lightsteelblue",       "lightyellow",
  "lime",            "limegreen",        "linen",                "magenta",
  "maroon",          "mediumaquamarine", "mediumblue",           "mediumorchid",
  "mediumpurple",    "mediumseagreen",   "mediumslateblue",      "mediumspringgreen",
  "mediumturquoise", "mediumvioletred",  "midnightblue",         "mintcream",
  "mistyrose",       "moccasin",         "navajowhite",          "navy",
  "oldlace",         "olive",            "olivedrab",            "orange",
  "orangered",       "orchid",           "palegoldenrod",        "palegreen",
  "paleturquoise",   "palevioletred",    "papayawhip",           "peachpuff",
  "peru",            "pink",             "plum",                 "powderblue",
  "purple",          "red",              "rosybrown",            "royalblue",
  "saddlebrown",     "salmon",           "sandybrown",           "seagreen",
  "seashell",        "sienna",           "silver",               "skyblue",
  "slateblue",       "slategrey",        "snow",                 "springgreen",
  "steelblue",       "tan",              "teal",                 "thistle",
  "tomato",          "turquoise",        "violet",               "wheat",
  "white",           "whitesmoke",       "yellow",               "yellowgreen",
];

var color = colors[0];

Stage {
  title: bind "Color.{color.toUpperCase()}"
  scene: Scene {
    fill: bind Color.web(color)
    width:  space + (size + space) * width
    height: space + (size + space) * height
    content: VBox {
      translateX: space
      translateY: space
      spacing: space
      content: for (v in [0..height-1])
        HBox {
          spacing: space
          content: for (h in [0..width-1])
            Rectangle {
              def color = colors[h + v * width];
              fill: Color.web(color)
              width:  size
              height: size
              onMouseEntered: function(event) {
                this.color = color
              }
            }
        }
    }
  }
}
