package org.jdesktop.swinghelper.layer.demo.util;

import java.awt.image.LookupTable;

/**
 * This class used as workaround for the buggy color convertion in *nix
 * java2D does instanceof checking looking for ByteLookupTable 
 * and turn the optimization on, this wrapper prevents the buggy optimization
 */
public class LookupTableWrapper extends LookupTable {
    private final LookupTable table;

    public LookupTableWrapper(LookupTable table) {
        super(table.getOffset(), table.getNumComponents());
        this.table = table;
    }

    public LookupTable getLookupTable() {
        return table;
    }

    public int[] lookupPixel(int[] src, int[] dest) {
        return table.lookupPixel(src, dest);
    }
}
