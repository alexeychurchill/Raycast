package io.github.alexeychurchill.raycast.game;

import io.github.alexeychurchill.raycast.game.math.Point;

/**
 * Player class
 */

public class Player {
    private Point position = new Point();
    private double angle = 0.0;

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void  addAngle(double delta) {
        this.angle += delta;
    }

    public double getAngle() {
        return angle;
    }

    public Point getPosition() {
        return position;
    }
}
