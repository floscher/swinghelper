package org.jdesktop.swinghelper.layer.effect;

import org.jdesktop.swinghelper.layer.JXLayer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;

public class ImageOpEffect<V extends JComponent> extends Effect<V> {
    private BufferedImageOp bufferedImageOp;

    public ImageOpEffect() {
    }

    public ImageOpEffect(BufferedImageOp bufferedImageOp) {
        this.bufferedImageOp = bufferedImageOp;
    }

    public BufferedImageOp getBufferedImageOp() {
        return bufferedImageOp;
    }

    public void setBufferedImageOp(BufferedImageOp bufferedImageOp) {
        this.bufferedImageOp = bufferedImageOp;
        fireStateChanged();
    }

    private boolean isInPlace() {
        return !(bufferedImageOp instanceof ConvolveOp);
    }

    public BufferedImage apply(BufferedImage buf, JXLayer<V> l) {
        if (bufferedImageOp != null) {
            if (isInPlace()) {
                return bufferedImageOp.filter(buf, buf);
            }
            BufferedImage dest =
                    bufferedImageOp.createCompatibleDestImage(buf, buf.getColorModel());
            return bufferedImageOp.filter(buf, dest);
        }
        return buf;
    }
}
