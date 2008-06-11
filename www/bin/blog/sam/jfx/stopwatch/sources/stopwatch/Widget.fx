/*
 * Widget.fx
 *
 * Created on Feb 26, 2008, 10:48:59 AM
 */

package stopwatch;

import javafx.gui.*;
import java.lang.System;

/**
 * @author Richard
 */
public class Widget {
    public attribute frame : Frame;
    public attribute canvas : Canvas;
    public attribute configCanvas : Canvas;
    
    /**
     * Given a correctly formatted string, converts the given string to a
     * color. The given string can be one of:
     * <ul>
     *  <li>RGB: where RGB are hex digits (0-F)</li>
     *  <li>RRGGBB: where R G and B are hex digits</li>
     *  <li>#RGB or #RRGGBB</li>
     *  <li>0xRGB or 0xRRGGBB</li>
     *  <li>Or a literal value such as:<ul>
     *      <li>Black</li>
     *      <li>Silver</li>
     *      <li>Gray</li>
     *      <li>White</li>
     *      <li>Maroon</li>
     *      <li>Red</li>
     *      <li>Purple</li>
     *      <li>Fuschia</li>
     *      <li>Green</li>
     *      <li>Lime</li>
     *      <li>Olive</li>
     *      <li>Yellow</li>
     *      <li>Navy</li>
     *      <li>Blue</li>
     *      <li>Teal</li>
     *      <li>Aqua</li></ul></li>
     * </ul>
     * 
     * Note that for all these values, both named and numeric, the case is
     * insignificant.
     */
    protected static function webcolor(color:String) : Color {
        var s = color.toLowerCase();
        var c = standardColors.get(s) as Color;
        if (c <> null) { return c; }
        
        //remove all non-hex digits
        if (s.startsWith("0x")) { s = s.substring(2); }
        s = s.replaceAll("[^0-9abcdef]", "");
        if (s.length() <> 3 and s.length() <> 6) {
            throw new java.lang.IllegalArgumentException("Bad color {color}");
        }
        return parseColor(s);
    }
    
    private static function parseColor(s:String) : Color {
        //if 3 chars then split those three into r, g, b
        if (s.length() == 3) {
            var r = java.lang.Integer.parseInt(s.substring(0, 1), 16);
            var g = java.lang.Integer.parseInt(s.substring(1, 2), 16);
            var b = java.lang.Integer.parseInt(s.substring(2, 3), 16);
            return Color.fromAWTColor(new java.awt.Color(r, g, b));
        } else if (s.length() == 6) {
            //if 6 chars then split those six into rr, gg, bb
            var r = java.lang.Integer.parseInt(s.substring(0, 2), 16);
            var g = java.lang.Integer.parseInt(s.substring(2, 4), 16);
            var b = java.lang.Integer.parseInt(s.substring(4, 6), 16);
            return Color.fromAWTColor(new java.awt.Color(r, g, b));
        } else {
            throw new java.lang.IllegalArgumentException("Bad color {s}");
        }
    }
    
    private static function populateStandardColors() : java.util.Map {
        var colors = new java.util.HashMap();
        colors.put("aliceblue",             parseColor("f0f8ff"));
        colors.put("antiquewhite",          parseColor("faebd7"));
        colors.put("aqua",                  parseColor("00ffff"));
        colors.put("aquamarine",            parseColor("7fffd4"));
        colors.put("azure",                 parseColor("f0ffff"));
        colors.put("beige",                 parseColor("f5f5dc"));
        colors.put("bisque",                parseColor("ffe4c4"));
        colors.put("black",                 parseColor("000000"));
        colors.put("blanchedalmond",        parseColor("ffebcd"));
        colors.put("blue",                  parseColor("0000ff"));
        colors.put("blueviolet",            parseColor("8a2be2"));
        colors.put("brown",                 parseColor("a52a2a"));
        colors.put("burlywood",             parseColor("deb887"));
        colors.put("cadetblue",             parseColor("5f9ea0"));
        colors.put("chartreuse",            parseColor("7fff00"));
        colors.put("chocolate",             parseColor("d2691e"));
        colors.put("coral",                 parseColor("ff7f50"));
        colors.put("cornflowerblue",        parseColor("6495ed"));
        colors.put("cornsilk",              parseColor("fff8dc"));
        colors.put("crimson",               parseColor("dc143c"));
        colors.put("cyan",                  parseColor("00ffff"));
        colors.put("darkblue",              parseColor("00008b"));
        colors.put("darkcyan",              parseColor("008b8b"));
        colors.put("darkgoldenrod",         parseColor("b8860b"));
        colors.put("darkgray",              parseColor("a9a9a9"));
        colors.put("darkgreen",             parseColor("006400"));
        colors.put("darkgrey",              parseColor("a9a9a9"));
        colors.put("darkkhaki",             parseColor("bdb76b"));
        colors.put("darkmagenta",           parseColor("8b008b"));
        colors.put("darkolivegreen",        parseColor("556b2f"));
        colors.put("darkorange",            parseColor("ff8c00"));
        colors.put("darkorchid",            parseColor("9932cc"));
        colors.put("darkred",               parseColor("8b0000"));
        colors.put("darksalmon",            parseColor("e9967a"));
        colors.put("darkseagreen",          parseColor("8fbc8f"));
        colors.put("darkslateblue",         parseColor("483d8b"));
        colors.put("darkslategray",         parseColor("2f4f4f"));
        colors.put("darkslategrey",         parseColor("2f4f4f"));
        colors.put("darkturquoise",         parseColor("00ced1"));
        colors.put("darkviolet",            parseColor("9400d3"));
        colors.put("deeppink",              parseColor("ff1493"));
        colors.put("deepskypblue",          parseColor("00bfff"));
        colors.put("dimgray",               parseColor("696969"));
        colors.put("dimgrey",               parseColor("696969"));
        colors.put("dodgerblue",            parseColor("1e90ff"));
        colors.put("firebrick",             parseColor("b22222"));
        colors.put("floralwhite",           parseColor("fffaf0"));
        colors.put("forestgreen",           parseColor("228b22"));
        colors.put("fuchsia",               parseColor("ff00ff"));
        colors.put("gainsboro",             parseColor("dcdcdc"));
        colors.put("ghostwhite",            parseColor("f8f8ff"));
        colors.put("gold",                  parseColor("ffd700"));
        colors.put("goldenrod",             parseColor("daa520"));
        colors.put("gray",                  parseColor("808080"));
        colors.put("green",                 parseColor("008000"));
        colors.put("greenyellow",           parseColor("adff2f"));
        colors.put("grey",                  parseColor("808080"));
        colors.put("honeydew",              parseColor("f0fff0"));
        colors.put("hotpink",               parseColor("ff69b4"));
        colors.put("indianred",             parseColor("cd5c5c"));
        colors.put("indigo",                parseColor("4b0082"));
        colors.put("ivory",                 parseColor("fffff0"));
        colors.put("khaki",                 parseColor("f0e68c"));
        colors.put("lavender",              parseColor("e6e6fa"));
        colors.put("lavenderblush",         parseColor("fff0f5"));
        colors.put("lawngreen",             parseColor("7cfc00"));
        colors.put("lemonchiffon",          parseColor("fffacd"));
        colors.put("lightblue",             parseColor("add8e6"));
        colors.put("lightcoral",            parseColor("f08080"));
        colors.put("lightcyan",             parseColor("e0ffff"));
        colors.put("lightgoldenrodyellow",  parseColor("fafad2"));
        colors.put("lightgray",             parseColor("d3d3d3"));
        colors.put("lightgreen",            parseColor("90ee90"));
        colors.put("lightgrey",             parseColor("d3d3d3"));
        colors.put("lightpink",             parseColor("ffb6c1"));
        colors.put("lightsalmon",           parseColor("ffa07a"));
        colors.put("lightseagreen",         parseColor("20b2aa"));
        colors.put("lightskyblue",          parseColor("87cefa"));
        colors.put("lightslategray",        parseColor("778899"));
        colors.put("lightslategrey",        parseColor("778899"));
        colors.put("lightsteelblue",        parseColor("b0c4de"));
        colors.put("lightyellow",           parseColor("ffffe0"));
        colors.put("lime",                  parseColor("00ff00"));
        colors.put("limegreen",             parseColor("32cd32"));
        colors.put("linen",                 parseColor("faf0e6"));
        colors.put("magenta",               parseColor("ff00ff"));
        colors.put("maroon",                parseColor("800000"));
        colors.put("mediumaquamarine",      parseColor("66cdaa"));
        colors.put("mediumblue",            parseColor("0000cd"));
        colors.put("mediumorchid",          parseColor("ba55d3"));
        colors.put("mediumpurple",          parseColor("9370db"));
        colors.put("mediumseagreen",        parseColor("3cb371"));
        colors.put("mediumslateblue",       parseColor("7b68ee"));
        colors.put("mediumspringgreen",     parseColor("00fa9a"));
        colors.put("mediumturquoise",       parseColor("48d1cc"));
        colors.put("mediumvioletred",       parseColor("c71585"));
        colors.put("midnightblue",          parseColor("191970"));
        colors.put("mintcream",             parseColor("f5fffa"));
        colors.put("mistyrose",             parseColor("ffe4e1"));
        colors.put("moccasin",              parseColor("ffe4b5"));
        colors.put("navajowhite",           parseColor("ffdead"));
        colors.put("navy",                  parseColor("000080"));
        colors.put("oldlace",               parseColor("fdf5e6"));
        colors.put("olive",                 parseColor("808000"));
        colors.put("olivedrab",             parseColor("6b8e23"));
        colors.put("orange",                parseColor("ffa500"));
        colors.put("orangered",             parseColor("ff4500"));
        colors.put("orchid",                parseColor("da70d6"));
        colors.put("palegoldenrod",         parseColor("eee8aa"));
        colors.put("palegreen",             parseColor("98fb98"));
        colors.put("paleturquoise",         parseColor("afeeee"));
        colors.put("palevioletred",         parseColor("db7093"));
        colors.put("papayawhip",            parseColor("ffefd5"));
        colors.put("peachpuff",             parseColor("ffdab9"));
        colors.put("peru",                  parseColor("cd853f"));
        colors.put("pink",                  parseColor("ffc0cb"));
        colors.put("plum",                  parseColor("dda0dd"));
        colors.put("powderblue",            parseColor("b0e0e6"));
        colors.put("purple",                parseColor("800080"));
        colors.put("red",                   parseColor("ff0000"));
        colors.put("rosybrown",             parseColor("bc8f8f"));
        colors.put("royalblue",             parseColor("4169e1"));
        colors.put("saddlebrown",           parseColor("8b4513"));
        colors.put("salmon",                parseColor("fa8072"));
        colors.put("sandybrown",            parseColor("f4a460"));
        colors.put("seagreen",              parseColor("2e8b57"));
        colors.put("seashell",              parseColor("fff5ee"));
        colors.put("sienna",                parseColor("a0522d"));
        colors.put("silver",                parseColor("c0c0c0"));
        colors.put("skyblue",               parseColor("87ceeb"));
        colors.put("slateblue",             parseColor("6a5acd"));
        colors.put("slategray",             parseColor("708090"));
        colors.put("slategrey",             parseColor("708090"));
        colors.put("snow",                  parseColor("fffafa"));
        colors.put("springgreen",           parseColor("00ff7f"));
        colors.put("steelblue",             parseColor("4682b4"));
        colors.put("tan",                   parseColor("d2b48c"));
        colors.put("teal",                  parseColor("008080"));
        colors.put("thistle",               parseColor("d8bfd8"));
        colors.put("tomato",                parseColor("ff6347"));
        colors.put("turquoise",             parseColor("40e0d0"));
        colors.put("violet",                parseColor("ee82ee"));
        colors.put("wheat",                 parseColor("f5deb3"));
        colors.put("white",                 parseColor("ffffff"));
        colors.put("whitesmoke",            parseColor("f5f5f5"));
        colors.put("yellow",                parseColor("ffff00"));
        colors.put("yellowgreen",           parseColor("9acd32"));
        return colors;
    }
    
    private static attribute standardColors = Widget.populateStandardColors();
    
    /**
     * creates and returns a Color for some rgb values.
     * @param r value between 0-255
     * @param g value between 0-255
     * @param b value between 0-255
     */
    protected static function rgb(r:Integer, g:Integer, b:Integer) : Color {
        return Color.fromAWTColor(new java.awt.Color(r, g, b));
    }
    
    /**
     * creates and returns a Color for some rgb values.
     * @param r value between 0-255
     * @param g value between 0-255
     * @param b value between 0-255
     * @param a value between 0-1
     */
    public static function rgba(r:Integer, g:Integer, b:Integer, a:Number) : Color {
        return Color.fromAWTColor(new java.awt.Color(r, g, b, (a * 255) as Integer));
    }
    
    /**
     * Creates and returns a Color for some hue saturation and brightness
     * @param h a value between 0-360
     * @param s a value between 0-1
     * @param b a value between 0-1
     */
    protected static function hsb(h:Integer, s:Number, b:Number) : Color {
        return Color.fromAWTColor(java.awt.Color.getHSBColor(360/h, s.floatValue(), b.floatValue()));
    }
    
    /**
     * Creates and returns a Color for some hue saturation and brightness
     * @param h a value between 0-360
     * @param s a value between 0-1
     * @param b a value between 0-1
     * @param a a value between 0-1
     */
    protected static function hsba(h:Integer, s:Number, b:Number, a:Number) : Color {
        var c = hsb(h, s, b);
        return Color.fromAWTColor(new java.awt.Color(c.red, c.green, c.blue, (a * 255) as Integer));
    }
}