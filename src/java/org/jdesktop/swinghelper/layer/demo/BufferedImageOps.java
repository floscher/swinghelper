package org.jdesktop.swinghelper.layer.demo;

import java.awt.image.*;
import java.awt.color.ColorSpace;
import java.util.Arrays;

/**
 * @author Alexander Potochkin
 * 
 * https://swinghelper.dev.java.net/
 * http://weblogs.java.net/blog/alexfromsun/ 
 */ 
public class BufferedImageOps {

    private BufferedImageOps() {
    }

    public static LookupOp getInvertColorOp() {
        return getInvertColorOp(true, true, true);
    }

    public static LookupOp getInvertColorOp(boolean red, boolean green, boolean blue) {
        short[] invert = new short[256];
        short[] straight = new short[256];
        for (int i = 0; i < invert.length; i++) {
            invert[i] = (short) (255 - i);
            straight[i] = (short) i;
        }
        short[][] result = new short[3][];
        Arrays.fill(result, straight);
        if (red) {
            result[0] = invert;
        }
        if (green) {
            result[1] = invert;
        }
        if (blue) {
            result[2] = invert;
        }
        LookupTable table = new ShortLookupTable(0, result);
        return new LookupOp(table, null);
    }

    public static LookupOp getPosterizeOp() {
        short[] posterize = new short[256];
        for (int i = 0; i < posterize.length; i++) {
            posterize[i] = (short) (i - (i % 32));
//            posterize[i] = (short) (i - (i % 256));
        }
        LookupTable table = new ShortLookupTable(0, posterize);
        return new LookupOp(table, null);
    }
    
    
    public static ColorConvertOp getGrayScaleOp() {
        return new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    }

    public static RescaleOp getRescaleOp(float scaleFactor) {
        return new RescaleOp(scaleFactor, 0, null);
    }

    public static RescaleOp getRescaleOp(float scaleFactor, float offset) {
        return new RescaleOp(scaleFactor, offset, null);
    }

    public static RescaleOp getRescaleOp(float[] scaleFactor) {
        float offset[] = new float[scaleFactor.length];
        Arrays.fill(offset, 0);
        return new RescaleOp(scaleFactor, offset, null);
    }

    public static RescaleOp getRescaleOp(float[] scaleFactor, float[] offset) {
        return new RescaleOp(scaleFactor, offset, null);
    }
    
    public static ConvolveOp getConvolveOp(int i) {
        float[] kernel = new float[i * i];
        Arrays.fill(kernel, 1f / (i * i));
        return new ConvolveOp(new Kernel(i, i, kernel), ConvolveOp.EDGE_NO_OP, null);
    }
}
