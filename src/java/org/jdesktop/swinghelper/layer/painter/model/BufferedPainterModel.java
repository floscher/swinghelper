package org.jdesktop.swinghelper.layer.painter.model;

public interface BufferedPainterModel extends PainterModel {
    
    public void setIgnorePartialRepaint(boolean ignorePartialRepaint);
    public boolean isIgnorePartialRepaint();

    public void setIncrementalUpdate(boolean incrementalUpdate);
    public boolean isIncrementalUpdate();
}
