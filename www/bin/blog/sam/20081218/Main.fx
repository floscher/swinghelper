import java.lang.Math;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
