import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.ext.swing.Canvas;
import javafx.ext.swing.CheckBox;
import javafx.ext.swing.ComponentView;
import javafx.ext.swing.GridPanel;
import javafx.ext.swing.Label;
import javafx.ext.swing.SwingFrame;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.geometry.Circle;
import javafx.scene.geometry.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

class SmoothSwitch {
  public attribute value = 0.0;
  public attribute speed = 300ms;
  public attribute selected = false on replace {
    timer.stop();
    var min = value;
    var max = if (selected)
            then 1.0
            else 0.0;
    var diff = if (selected)
             then max - min
             else min - max;
    timer = Timeline {
      keyFrames: [
        KeyFrame {time: 0ms            values: value => min},
        KeyFrame {time: speed * diff   values: value => max tween Interpolator.LINEAR},
      ]
    };
    timer.start();
  }
  private attribute timer: Timeline;
}

public class Nodes extends CustomNode {

  private attribute x: Number;
  private attribute y: Number;
  private attribute angle: Number;

  private attribute text  = SmoothSwitch {speed: 1s};
  private attribute shape = SmoothSwitch {speed: 1s};
  private attribute image = SmoothSwitch {speed: 1s};
  private attribute alpha = SmoothSwitch {speed: 1s};
  private attribute jump  = SmoothSwitch {speed: 1s};
  private attribute scale = SmoothSwitch {selected: bind text.selected or shape.selected or image.selected};

  private attribute panel: GridPanel = GridPanel {
    //border: TitledBorder {title: "Customize:"}
    rows:    6
    columns: 1
    content: [
      Label    {text: "Customize:"},
      CheckBox {text: "Use text"    action: update    selected: bind  text.selected with inverse},
      CheckBox {text: "Use shape"   action: update    selected: bind shape.selected with inverse},
      CheckBox {text: "Use image"   action: update    selected: bind image.selected with inverse},
      CheckBox {text: "Animation"   action: animate   selected: bind  jump.selected with inverse   enabled: bind scale.selected},
      CheckBox {text: "Translucency"                  selected: bind alpha.selected with inverse},
    ]
  }

  function create() {
    Group {
      content: [
        Rectangle {
          width:  bind 3 * panel.width
          height: bind 3 * panel.height
          fill: LinearGradient {
            endY: 400
            proportional: false
            stops: [
              Stop {offset: 0     color: Color.SILVER},
              Stop {offset: 0.6   color: Color.LIGHTBLUE},
              Stop {offset: 1     color: Color.BLUE}
            ]
          }
        },
        Group {
          transform: bind Transform.rotate(angle, 50, 50)
          translateX: bind x
          translateY: bind y
          content: [
            Circle {
              opacity: bind shape.value
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
              opacity: bind text.value
              content: "Duke"
            },
            ImageView {
              transform: bind Transform.translate(31, 27)
              opacity: bind image.value
              image: Image {url: this.getClass().getResource("duke.png").toString()}
            },
          ]
        },
        ComponentView {
          transform: bind Transform.scale(3 - 2 * scale.value, 3 - 2 * scale.value)
          translateX: bind 1.8 * scale.value * panel.width
          translateY: bind 1.8 * scale.value * panel.height
          opacity: bind 1.0 - 0.6 * alpha.value
          component: bind panel
        }
      ]
    }
  }

  private function update(): Void {
    if (not scale.selected) {
      jump.selected = false;
      timer.pause();
    }
  }

  private attribute timer: Timeline;
  private function animate(): Void {
    if (jump.selected) {
      if (timer == null) {
        timer = Timeline {
          keyFrames: KeyFrame {
            timelines: [
              Timeline {
                var max = bind 3 * panel.width - 100;
                repeatCount: Timeline.INDEFINITE
                autoReverse: true
                keyFrames: [
                  KeyFrame {time: 0s   values: [
                    x     => 0.0,
                    angle => 0.0,
                  ]},
                  KeyFrame {time: 6s   values: [
                    x     => max tween Interpolator.LINEAR,
                    angle => 360 tween Interpolator.LINEAR,
                  ]}
                ]
              },
              Timeline {
                var max = bind 3 * panel.height - 100;
                repeatCount: Timeline.INDEFINITE
                keyFrames: [
                  KeyFrame {time:   0s   values: y => 0.0},
                  KeyFrame {time: 2.2s   values: y => max tween Interpolator.SPLINE(0, 0, .9, 0)},
                  KeyFrame {time: 2.3s   values: y => max},
                  KeyFrame {time: 4.5s   values: y => 0.0 tween Interpolator.SPLINE(0, 0, 0, .9)}
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
}

SwingFrame {
  title: "Nodes (JavaFX demo)"
  content: Canvas {
    content: Nodes {}
  }
  visible: true
  //TODO: temporary solution
  closeAction: function() {
    java.lang.System.exit(0);
  }
}
