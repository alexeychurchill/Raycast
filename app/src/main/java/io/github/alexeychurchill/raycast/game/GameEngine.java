package io.github.alexeychurchill.raycast.game;

/**
 * Game engine
 * Main game class
 */

public class GameEngine {
    private Player player = new Player();
    private Map map = new Map();
    private double angleOfView = 60.0;

    public GameEngine() {
        player.getPosition().x(1.0);
        player.getPosition().y(1.0);
        player.setAngle(0.0);
    }

    public Player getPlayer() {
        return player;
    }

    public void setAngleOfView(double angleOfView) {
        this.angleOfView = angleOfView;
    }

    public double getAngleOfView() {
        return angleOfView;
    }

    public Map getMap() {
        return map;
    }
}
