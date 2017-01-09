package io.github.alexeychurchill.raycast.game;

import io.github.alexeychurchill.raycast.game.math.LineSegment;

/**
 * Wall class
 */

public class Wall extends LineSegment {
    private int typeId = -1;

    public Wall(double x0, double y0, double x1, double y1, int typeId) {
        super(x0, y0, x1, y1);
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
}
