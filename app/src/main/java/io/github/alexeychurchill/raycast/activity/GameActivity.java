package io.github.alexeychurchill.raycast.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.alexeychurchill.raycast.R;
import io.github.alexeychurchill.raycast.game.GameEngine;
import io.github.alexeychurchill.raycast.view.JoystickView;
import io.github.alexeychurchill.raycast.view.RaycastRenderer;
import io.github.alexeychurchill.raycast.view.RenderView;

/**
 * Main game activity
 */

public class GameActivity extends AppCompatActivity implements
        JoystickView.OnJoystickChangeListener {

    private static final double MAX_ANGLE_DELTA = 2.5f;
    private static final double MAX_SPEED = 0.1;
    private GameEngine gameEngine = new GameEngine();
    private RaycastRenderer renderer = new RaycastRenderer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        renderer.setGameEngine(gameEngine);

        RenderView renderView = ((RenderView) findViewById(R.id.rvGame));

        renderView.setRenderer(renderer);

        JoystickView joystickView = ((JoystickView) findViewById(R.id.jvJoystick));
        if (joystickView != null) {
            joystickView.setChangeListener(this);
        }
    }

    @Override
    public void onJoystickChange(float xValue, float yValue) {
        gameEngine.getPlayer().addAngle(MAX_ANGLE_DELTA * xValue);
        double playerAngle = gameEngine.getPlayer().getAngle() / 180.0 * Math.PI;

        double xPosition = gameEngine.getPlayer().getPosition().x() + MAX_SPEED * Math.cos(playerAngle) * yValue;
        gameEngine.getPlayer().getPosition().x(xPosition);

        double yPosition = gameEngine.getPlayer().getPosition().y() + MAX_SPEED * Math.sin(playerAngle) * yValue;
        gameEngine.getPlayer().getPosition().y(yPosition);
    }
}
