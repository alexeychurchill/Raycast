package io.github.alexeychurchill.raycast.game.math;

/**
 * Line class
 */

public class Line {
    private double x0 = 0.0;
    private double y0 = 0.0;
    private double ax = 0.0;
    private double ay = 0.0;

    public Line(double x0, double y0, double ax, double ay) {
        this.x0 = x0;
        this.y0 = y0;
        this.ax = ax;
        this.ay = ay;
    }

    public double getX0() {
        return x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getY0() {
        return y0;
    }

    public void setY0(double y0) {
        this.y0 = y0;
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public double x(double t) {
        return x0 + ax * t(t);
    }

    public double y(double t) {
        return y0 + ay * t(t);
    }

    public boolean isTOk(double t) {
        return true;
    }

    protected double t(double t) {
        return t;
    }
}
