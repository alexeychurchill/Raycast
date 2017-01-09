package io.github.alexeychurchill.raycast.game.math;

/**
 * Point class
 */

public class Point {
    private double x = 0.0;
    private double y = 0.0;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public void x(double x) {
        this.x = x;
    }

    public void y(double y) {
        this.y =  y;
    }
}
