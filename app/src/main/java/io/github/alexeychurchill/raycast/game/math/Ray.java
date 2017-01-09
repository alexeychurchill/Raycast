package io.github.alexeychurchill.raycast.game.math;

/**
 * Ray class
 */

public class Ray extends Line {
    public Ray(double x0, double y0, double angle) {
        super(x0, y0, Math.cos(angle), Math.sin(angle));
    }

    public void setAngle(double angle) {
        setAx(Math.cos(angle));
        setAy(Math.sin(angle));
    }

    @Override
    protected double t(double t) {
        return (t >= 0.0) ? t : 0.0;
    }

    @Override
    public boolean isTOk(double t) {
        return (t >= 0.0);
    }
}
