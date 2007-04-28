package org.jdesktop.swinghelper.layer.painter.model;

public class DefaultBufferedPainterModel extends DefaultPainterModel implements BufferedPainterModel {
    private boolean isIncrementalUpdate;
    private boolean isIgnorePartialRepaint;

    public DefaultBufferedPainterModel() {
        isIncrementalUpdate = true;
    }

    public boolean isIgnorePartialRepaint() {
        return isIgnorePartialRepaint;
    }
    
    public void setIgnorePartialRepaint(boolean isIgnorePartialRepaint) {
        this.isIgnorePartialRepaint = isIgnorePartialRepaint;
        fireLayerItemChanged();
    }
    
    public boolean isIncrementalUpdate() {
        return isIncrementalUpdate;
    }

    public void setIncrementalUpdate(boolean isIncrementalUpdate) {
        this.isIncrementalUpdate = isIncrementalUpdate;
        fireLayerItemChanged();
    }
}
