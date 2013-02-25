
package com.nebhale.cyclinglibrary.model;

public final class StubPointFactory {

    public static Point create(int base) {
        return new Point(Double.valueOf(base), Double.valueOf(base + 1), Double.valueOf(base + 2));
    }
}
