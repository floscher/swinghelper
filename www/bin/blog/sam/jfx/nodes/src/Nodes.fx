import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.gui.Canvas;
import javafx.gui.CheckBox;
import javafx.gui.Circle;
import javafx.gui.Color;
import javafx.gui.ComponentView;
import javafx.gui.Frame;
import javafx.gui.GridPanel;
import javafx.gui.Group;
import javafx.gui.Image;
import javafx.gui.ImageView;
import javafx.gui.Label;
import javafx.gui.LinearGradient;
import javafx.gui.RadialGradient;
import javafx.gui.Rectangle;
import javafx.gui.Stop;
import javafx.gui.Text;
//import javafx.gui.TitledBorder;
import javafx.gui.Transform;

class Point {
  attribute x: Number;
  attribute y: Number;
}

class Bounds extends Point {
  attribute width: Integer;
  attribute height: Integer;
}

class Model {
  attribute panel = Bounds {};
  attribute scale: Number = 3;
  attribute opacity: Number = 1;

  attribute angle: Number = 0;
  attribute point = Point {};
  attribute timer: Timeline;

  attribute text: Boolean;
  attribute shape: Boolean;
  attribute image: Boolean;
  attribute animation: Boolean;
  attribute translucency: Boolean;

  function update(): Void {
    if (text or shape or image) {
      scale = 1;
    } else {
      scale = 3;
      animation = false;
    }
    panel.x = 0.9 * (3 - scale) * panel.width;
    panel.y = 0.9 * (3 - scale) * panel.height;
    opacity = if (translucency) then 0.4 else 1.0;
    if (animation) {
      if (timer == null) {
        timer = Timeline {
          repeatCount: Timeline.INDEFINITE
          keyFrames: KeyFrame {
            time: 0s
            timelines: [
              Timeline {
                var max = bind 3 * panel.width - 100;
                repeatCount: Timeline.INDEFINITE
                autoReverse: true
                keyFrames: [
                  KeyFrame {time: 0s   values: [
                    angle   => 0.0,
                    point.x => 0.0,
                  ]},
                  KeyFrame {time: 6s   values: [
                    angle   => 360.0 tween Interpolator.LINEAR,
                    point.x => max   tween Interpolator.LINEAR,
                  ]}
                ]
              },
              Timeline {
                var max = bind 3 * panel.height - 100;
                repeatCount: Timeline.INDEFINITE
                keyFrames: [
                  KeyFrame {time:   0s   values: point.y => 0.0},
                  KeyFrame {time: 2.2s   values: point.y => max tween Interpolator.SPLINE(0, 0, .9, 0)},
                  KeyFrame {time: 2.3s   values: point.y => max},
                  KeyFrame {time: 4.5s   values: point.y => 0.0 tween Interpolator.SPLINE(0, 0, 0, .9)}
                ]
              }
            ]
          }
        };
        timer.start();
      } else {
        timer.resume();
      }
    } else {
      timer.pause();
    }
  }
  public function load(name: String): String {
    this.getClass().getResource(name).toString()
  }
}

Frame {
  var model = Model {}
  title: "Nodes (JavaFX demo)"
  content: Canvas {
    content: [
      Rectangle {
        width: bind 3 * model.panel.width
        height: bind 3 * model.panel.height
        fill: LinearGradient {
          endY: 450
          proportional: false
          stops: [
            Stop {offset: 0     color: Color.SILVER},
            Stop {offset: 0.5   color: Color.LIGHTBLUE},
            Stop {offset: 1     color: Color.BLUE}
          ]
        }
      },
      Group {
        transform: bind [
          Transform.translate(model.point.x, model.point.y),
          Transform.rotate(model.angle, 50, 50),
        ]
        content: [
          Circle {
            visible: bind model.shape
            centerX: 50   centerY: 50   radius: 50
            stroke: Color.YELLOW
            fill: RadialGradient {
              centerX: 50   centerY: 50   radius: 50
              focusX: 70   focusY: 30
              proportional: false
              stops: [
                Stop {offset: 0   color: Color.YELLOW},
                Stop {offset: 1   color: Color.WHITE}
              ]
            }
          },
          Text {
            transform: bind Transform.rotate(33, 10, 100)
            visible: bind model.text
            content: "Duke"
          },
          ImageView {
            transform: bind Transform.translate(31, 27)
            visible: bind model.image
            image: Image {url: model.load("duke.png")}
          },
        ]
      },
      ComponentView {
        transform: bind [
          Transform.translate(model.panel.x, model.panel.y),
          Transform.scale(model.scale, model.scale)
        ]
        opacity: bind model.opacity
        component: GridPanel {
          //border: TitledBorder {title: "Customize:"}
          width: bind model.panel.width with inverse
          height: bind model.panel.height with inverse
          columns: 1
          rows: 6
          content: [
            Label    {text: "Customize:"},
            CheckBox {text: "Use text"       action: model.update   selected: bind model.text with inverse},
            CheckBox {text: "Use shape"      action: model.update   selected: bind model.shape with inverse},
            CheckBox {text: "Use image"      action: model.update   selected: bind model.image with inverse},
            CheckBox {text: "Animation"      action: model.update   selected: bind model.animation with inverse   enabled: bind model.text or model.shape or model.image},
            CheckBox {text: "Translucency"   action: model.update   selected: bind model.translucency with inverse},
          ]
        }
      }
    ]
  }
  visible: true
}
