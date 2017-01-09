package io.github.alexeychurchill.raycast.game.math;

/**
 * Line segment class
 */

public class LineSegment extends Line {
    public LineSegment(double x0, double y0, double x1, double y1) {
        super(x0, y0, (x1 - x0), (y1 - y0));
    }

    public LineSegment(Point p0, Point p1) {
        this(p0.x(), p0.y(), p1.x(), p1.y());
    }

    @Override
    public boolean isTOk(double t) {
        return (0.0 <= t) && (t <= 1.0);
    }

    @Override
    protected double t(double t) {
        if (t > 1.0) {
            return 1.0;
        }
        if (t < 0.0) {
            return 0.0;
        }
        return t;
    }
}
