/*
 * StopwatchWidget.fx
 *
 * Created on Feb 26, 2008, 10:20:29 AM
 */

package stopwatch;

import java.lang.*;
import javafx.input.*;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.swing.*;
import javafx.scene.text.*;
import javafx.scene.effect.*;
import javafx.scene.effect.light.*;
import stopwatch.Widget;
import stopwatch.ShapedWindowHelper;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class StopwatchWidget extends Widget {
    public attribute symbols : String[];
    private attribute startTime:Integer = 0;
    private attribute handAngle:Number = 180;
    private attribute tenthsHandAngle:Number = 180;
    private attribute hundredthsHandAngle:Number = 180;
    private attribute timeString:String = "00:00:00";
    private attribute resetPressedAmount:Integer = 7;
    private attribute startPressedAmount:Integer = 7;

    private attribute timerListener:ActionListener = ActionListener {
        public function actionPerformed(evt:ActionEvent): Void {
            if (startTime == 0) startTime = System.currentTimeMillis() as Integer;
            var elapsedMillis:Integer = ((System.currentTimeMillis() as Integer) - startTime);
            var elapsedHundredthsSecond:Integer = ((System.currentTimeMillis() as Integer) - startTime)/10;

            var hundredthsExact:Number = (elapsedMillis/10.0)%10;
            var tenthsExact:Number = (elapsedMillis/100.0)%100;

            var hundredths:Integer = elapsedHundredthsSecond%10;
            var tenths:Integer = (elapsedHundredthsSecond/10)%10;
            var seconds:Integer = (elapsedHundredthsSecond/100)%60;

            handAngle = 180+6*seconds;
            tenthsHandAngle = 180+36*tenthsExact;
            hundredthsHandAngle = 180+36*hundredthsExact;

            var decimalSeconds:Number = (elapsedHundredthsSecond/100.0)%60.0;
            var mins:Integer = elapsedHundredthsSecond/6000;

            timeString = "{%02d mins}:{%05.2f decimalSeconds}";
        }
    }

    private attribute timer:Timer = new Timer(57, timerListener);

    init {
        var numOfMarks = 12;
        var numOfMinorMarks = 60;
        var marks = for (i in [1..numOfMarks]) {
            Rectangle{x: 0-2 y: 108 width: 4 height: 13
                    fill: webcolor("#9fff81")
                    rotate: (360/numOfMarks)*i
                    translateX: 140
                    translateY: 140}
        };
        var minorMarks = for (i in [1..numOfMinorMarks]) {
            Line{startX: 0 startY: 120 endX: 0 endY: 114
                    stroke: webcolor("#FFFFFF")
                    rotate: (360/numOfMinorMarks)*i
                    translateX: 140
                    translateY: 140}
        };
        var hundredthsNumOfMarks = 10;
        var hundredthsNumOfMinorMarks = 50;
        var hundredthsMarks = for (i in [1..hundredthsNumOfMarks]) {
            Rectangle{x: -1 y: 24 width: 2 height: 6
                    fill: webcolor("#9fff81")
                    rotate: (360/hundredthsNumOfMarks)*i}
        };
        var hundredthsMinorMarks = for (i in [1..hundredthsNumOfMinorMarks]) {
            Line{startX: 0 startY: 30 endX: 0 endY: 28
                    stroke: webcolor("#FFFFFF")
                    rotate: (360/hundredthsNumOfMinorMarks)*i}
        };
        var tenthsNumOfMarks = 10;
        var tenthsNumOfMinorMarks = 50;
        var tenthsMarks = for (i in [1..tenthsNumOfMarks]) {
            Rectangle{x: -1 y: 24 width: 2 height: 6
                    fill: webcolor("#9fff81")
                    rotate: (360/tenthsNumOfMarks)*i}
        };
        var tenthsMinorMarks = for (i in [1..tenthsNumOfMinorMarks]) {
            Line{startX: 0 startY: 30 endX: 0 endY: 28
                    stroke: webcolor("#FFFFFF")
                    rotate: (360/tenthsNumOfMinorMarks)*i}
        };

        canvas = Canvas {
            content: [
                // MAIN DIAL
                Group { translateX: 40 translateY: 40 content: [
                    Circle {
                        centerX: 140
                        centerY: 140
                        radius: 140
                        fill:LinearGradient { startX:0 startY:0 endX:1 endY:1
                            stops: [
                                Stop { offset:0 color: webcolor("#3c3c3c") },
                                Stop { offset:1 color: webcolor("#010101") }
                            ]
                        }
                    },
                    Circle {
                        centerX: 140
                        centerY: 140
                        radius: 134
                        fill:RadialGradient { centerX:0.5 centerY:0.5 radius:0.5
                            stops: [
                                Stop { offset:0 color: rgba(20,20,20,1) },
                                Stop { offset:0.9499 color: rgba(20,20,20,1) },
                                Stop { offset:0.95 color: rgba(20,20,20,1) },
                                Stop { offset:0.975 color: rgba(20,20,20,1) },
                                Stop { offset:1 color: rgba(84,84,84,0) }
                            ]
                        }
                    },
                    // Tick Marks
                    Group { content: bind for (m in marks) { m }},
                    Group { content: bind for (m in minorMarks) { m }},
                    // Numbers
                    Text {
                        font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                        x: 141 y: 244
                        content: "30"
                        horizontalAlignment: HorizontalAlignment.CENTER
                        fill: webcolor("#FFFFFF")
                    },
                    Text {
                        font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                        x: 141 y: 47
                        content: "0"
                        horizontalAlignment: HorizontalAlignment.CENTER
                        fill: webcolor("#FFFFFF")
                    },
                    Text {
                        font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                        x: 38 y: 144
                        content: "45"
                        verticalAlignment: VerticalAlignment.CENTER
                        textOrigin: TextOrigin.TOP
                        fill: webcolor("#FFFFFF")
                    },
                    Text {
                        font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                        x: 225 y: 145
                        content: "15"
                        verticalAlignment: VerticalAlignment.CENTER
                        textOrigin: TextOrigin.TOP
                        fill: webcolor("#FFFFFF")
                    },
                    // Tenths Dial
                    Group {
                        content: [
                            Text {
                                font: Font{name: "Arial" size: 9}
                                x: 0 y: 10
                                content: "10ths"
                                horizontalAlignment: HorizontalAlignment.CENTER
                                textOrigin: TextOrigin.TOP
                                fill: webcolor("#FFFFFF")
                            },
                            // Tick Marks
                            Group { content: bind for (m in tenthsMarks) { m }},
                            Group { content: bind for (m in tenthsMinorMarks) { m }},
                            // Hand
                            Group { content: [
                                    Circle {centerX: 0 centerY: 0 radius: 3
                                        fill: webcolor("#FFFFFF")},
                                    Rectangle{x: -1 y: -8 width: 2 height: 35
                                            fill: webcolor("#FFFFFF")
                                            rotate: bind tenthsHandAngle},
                                ]}
                        ]
                        translateX: 100 translateY: 100
                    },
                    // Hundredths Dial
                    Group {
                        content: [
                            Text {
                                font: Font{name: "Arial" size: 9}
                                x: 0 y: 10
                                content: "100ths"
                                horizontalAlignment: HorizontalAlignment.CENTER
                                textOrigin: TextOrigin.TOP
                                fill: webcolor("#FFFFFF")
                            },
                            // Tick Marks
                            Group { content: bind for (m in hundredthsMarks) { m }},
                            Group { content: bind for (m in hundredthsMinorMarks) { m }},
                            // Hand
                            Group { content: [
                                    Circle {centerX: 0 centerY: 0 radius: 3
                                        fill: webcolor("#FFFFFF")},
                                    Rectangle{x: -1 y: -8 width: 2 height: 35
                                            fill: webcolor("#FFFFFF")
                                            rotate: bind hundredthsHandAngle},
                                ]}
                        ]
                        translateX: 180 translateY: 100
                    },
                    // Digits
                    Group {
                        content: [
                            Rectangle{x: -7 y: -9 width: 15 height: 19 fill: webcolor("#000000") effect: Lighting{}},
                            Rectangle{x: -5 y: -7 width: 11 height: 15 fill: webcolor("#FFFFFF") effect: Lighting{}},
                            Text {font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                                x: -4 y: 6 content: bind timeString.substring(0,1)
                                fill: webcolor("#000000")},]
                        translateX: 91 translateY: 210
                    },
                    Group {
                        content: [
                            Rectangle{x: -7 y: -9 width: 15 height: 19 fill: webcolor("#000000") effect: Lighting{}},
                            Rectangle{x: -5 y: -7 width: 11 height: 15 fill: webcolor("#FFFFFF") effect: Lighting{}},
                            Text {font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                                x: -4 y: 6 content: bind timeString.substring(1,2)
                                fill: webcolor("#000000")},]
                        translateX: 107 translateY: 210
                    },
                    Text {
                        font: Font{name: "Courier" size: 16}
                        x: 114 y: 214
                        content: ":"
                        fill: webcolor("#FFFFFF")
                    },
                    Group {
                        content: [
                            Rectangle{x: -7 y: -9 width: 15 height: 19 fill: webcolor("#000000") effect: Lighting{}},
                            Rectangle{x: -5 y: -7 width: 11 height: 15 fill: webcolor("#FFFFFF") effect: Lighting{}},
                            Text {font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                                x: -4 y: 6 content: bind timeString.substring(3,4)
                                fill: webcolor("#000000")},]
                        translateX: 130 translateY: 210
                    },
                    Group {
                        content: [
                            Rectangle{x: -7 y: -9 width: 15 height: 19 fill: webcolor("#000000") effect: Lighting{}},
                            Rectangle{x: -5 y: -7 width: 11 height: 15 fill: webcolor("#FFFFFF") effect: Lighting{}},
                            Text {font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                                x: -4 y: 6 content: bind timeString.substring(4,5)
                                fill: webcolor("#000000")},]
                        translateX: 146 translateY: 210
                    },
                    Text {
                        font: Font{name: "Courier" size: 16}
                        x: 153 y: 214
                        content: "."
                        fill: webcolor("#FFFFFF")
                    },
                    Group {
                        content: [
                            Rectangle{x: -7 y: -9 width: 15 height: 19 fill: webcolor("#000000") effect: Lighting{}},
                            Rectangle{x: -5 y: -7 width: 11 height: 15 fill: webcolor("#FF0000") effect: Lighting{}},
                            Text {font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                                x: -4 y: 6 content: bind timeString.substring(6,7)
                                fill: webcolor("#FFFFFF")},]
                        translateX: 168 translateY: 210
                    },
                    Group {
                        content: [
                            Rectangle{x: -7 y: -9 width: 15 height: 19 fill: webcolor("#000000") effect: Lighting{}},
                            Rectangle{x: -5 y: -7 width: 11 height: 15 fill: webcolor("#FF0000") effect: Lighting{}},
                            Text {font: Font{name: "Courier" size: 16, style: FontStyle.BOLD}
                                x: -4 y: 6 content: bind timeString.substring(7,8)
                                fill: webcolor("#FFFFFF")},]
                        translateX: 184 translateY: 210
                    },
                    // Hand
                    Group {
                        content: [
                            Group {
                                content: [
                                    Circle {centerX: 140 centerY: 140 radius: 8
                                        fill: webcolor("#FF0000")},
                                    Rectangle{x: -1.5 y: -20 width: 3 height: 120
                                            fill: webcolor("#FF0000")
                                            rotate: bind handAngle
                                            translateX: 140
                                            translateY: 140},
                                    Rectangle{x: -1.5 y: -40 width: 3 height: 20
                                            fill: webcolor("#FFFFFF")
                                            rotate: bind handAngle
                                            translateX: 140
                                            translateY: 140}

                                ]
                                effect: Lighting{
                                    light: DistantLight {azimuth: 225}
                                }
    //                            effect: Blend{
    //                                topInput: DropShadow {offsetX: 4 offsetY: 4 radius: 6 color: webcolor("#000000")}
    //                                bottomInput: Lighting{ light: DistantLight {azimuth: 225}}
    //                            }
                            }
                        ]
                        effect: DropShadow {offsetX: 4 offsetY: 4 radius: 6 color: webcolor("#000000")}
                    },
                    // Highlight
                    Group {
                        content: [
                            Arc { centerX: 15 centerY: 15 radiusX:250 radiusY: 250 type: ArcType.CHORD
                                        startAngle: 200 length: -130
                                        fill: rgb(255,255,255)},
                            Arc { centerX: 15 centerY: 15 radiusX:250 radiusY: 250 type: ArcType.CHORD
                                        startAngle: 190 length: -122
                                        fill: rgb(255,255,255)},
                        ]
                        effect: GaussianBlur{ radius: 2 }
                        opacity: 0.05
                    },
                ]},
                // Reset Button
                Group {
                    content: [
                        Rectangle{x: -7 y: bind -(150+resetPressedAmount) width: 14 height: 7
                            fill: LinearGradient { startX:0 startY:0 endX:1 endY:0
                                stops: [
                                    Stop { offset:0 color: webcolor("#AA0000") },
                                    Stop { offset:0.5 color: webcolor("#660000") },
                                    Stop { offset:1 color: webcolor("#AA0000") }
                                ]
                            }
                        },
                        Rectangle{x: -12 y: -150 width: 24 height: 14
                            fill: LinearGradient { startX:0 startY:0 endX:1 endY:0
                                stops: [
                                    Stop { offset:0 color: webcolor("#DDDDDD") },
                                    Stop { offset:0.5 color: webcolor("#AAAAAA") },
                                    Stop { offset:1 color: webcolor("#DDDDDD") }
                                ]
                            }
                        },
                        Rectangle{x: -14 y: bind -(155+resetPressedAmount) width: 28 height: 5
                            fill: LinearGradient { startX:0 startY:0 endX:1 endY:0
                                stops: [
                                    Stop { offset:0 color: webcolor("#FF0000") },
                                    Stop { offset:0.5 color: webcolor("#AA0000") },
                                    Stop { offset:1 color: webcolor("#FF0000") }
                                ]
                            }
                        },
                    ]
                    rotate: -30
                    translateX: 180 translateY: 180
                    onMousePressed: function(e:MouseEvent) {
                        resetPressedAmount=0;
                        //TODO: ShapedWindowHelper.refreshShape(frame.getJFrame());
                    }
                    onMouseReleased: function(e:MouseEvent) {
                        resetPressedAmount=7;
                        //TODO: ShapedWindowHelper.refreshShape(frame.getJFrame());
                    }
                    onMouseClicked: function(e:MouseEvent) {
                        reset();
                    }
                    cursor: Cursor.HAND
                },
                // Start/Stop Button
                Group {
                    content: [
                        Rectangle{x: -7 y: bind -(150+startPressedAmount) width: 14 height: 7
                            fill: LinearGradient { startX:0 startY:0 endX:1 endY:0
                                stops: [
                                    Stop { offset:0 color: webcolor("#8cc700") },
                                    Stop { offset:0.5 color: webcolor("#71a000") },
                                    Stop { offset:1 color: webcolor("#8cc700") }
                                ]
                            }
                        },
                        Rectangle{x: -12 y: -150 width: 24 height: 14
                            fill: LinearGradient { startX:0 startY:0 endX:1 endY:0
                                stops: [
                                    Stop { offset:0 color: webcolor("#DDDDDD") },
                                    Stop { offset:0.5 color: webcolor("#AAAAAA") },
                                    Stop { offset:1 color: webcolor("#DDDDDD") }
                                ]
                            }
                        },
                        Rectangle{x: -14 y: bind -(155+startPressedAmount) width: 28 height: 5
                            fill: LinearGradient { startX:0 startY:0 endX:1 endY:0
                                stops: [
                                    Stop { offset:0 color: webcolor("#b4FF00") },
                                    Stop { offset:0.5 color: webcolor("#8cc700") },
                                    Stop { offset:1 color: webcolor("#b4FF00") }
                                ]
                            }
                        },
                    ]
                    rotate: 30
                    translateX: 180 translateY: 180
                    onMousePressed: function(e:MouseEvent) {
                        startPressedAmount=0;
                        //TODO: ShapedWindowHelper.refreshShape(frame.getJFrame());
                    }
                    onMouseReleased: function(e:MouseEvent) {
                        startPressedAmount=7;
                        //TODO: ShapedWindowHelper.refreshShape(frame.getJFrame());
                    }
                    onMouseClicked: function(e:MouseEvent) {
                        startStop();
                    }
                    cursor: Cursor.HAND
                },
            ]
        }
    }

    function startStop(){
        // if started
        if (timer.isRunning()){
            timer.stop();
        } else {
            timer.start();
        }
    }

    function reset(){
        // if started
        if (timer.isRunning()){
            timer.stop();
        }
        startTime = 0;
        timerListener.actionPerformed(null);
    }
}
