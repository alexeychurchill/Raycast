package io.github.alexeychurchill.raycast.view;

import android.app.Application;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import io.github.alexeychurchill.raycast.game.GameEngine;
import io.github.alexeychurchill.raycast.game.Map;
import io.github.alexeychurchill.raycast.game.Wall;

/**
 * Raycaster
 */

public class RaycastRenderer {
    private static final double EPS = 0.00001;
    private Paint paint = new Paint();
    private GameEngine gameEngine;

    private int cells = 512;
    private boolean[][] field = new boolean[cells][cells];

    public RaycastRenderer() {
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void render(Canvas canvas, int width, int height) {
        // Background
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(0.0f, 0.0f, width, 0.5f * height, paint);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(0.0f, 0.5f * height, width, height, paint);
        paint.setColor(Color.RED);

        double angleOfView = gameEngine.getAngleOfView();
        double anglePerColumn = angleOfView / width;

        double xPlayer = gameEngine.getPlayer().getPosition().x();
        double yPlayer = gameEngine.getPlayer().getPosition().y();

        double anglePlayer = gameEngine.getPlayer().getAngle();

        for (int x = 0; x < width; x++) {
            double xAngle = -angleOfView / 2.0 + x * anglePerColumn + anglePlayer;

            CastResult castResult = traceRay(xAngle);

            if (castResult == null) {
                continue;
            }

            int r = 0, g = 0, b = 0;

            switch (castResult.blockType) {
                case 1:
                    r = 255; g = 0; b = 0;
                    break;
                case 2:
                    r = 0; g = 255; b = 0;
                    break;
                case 3:
                    r = 0; g = 0; b = 255;
                    break;
                case 4:
                    r = 127; g = 127; b = 127;
                    break;
            }

            double angleRad = (-angleOfView / 2.0 + x * anglePerColumn) / 180.0 * Math.PI;

            double distance = castResult.distance(xPlayer, yPlayer) * Math.cos(angleRad);

            double light = 1.0 - (distance / 20.0);

            int color = Color.rgb((int) (r * light), (int) (g * light), (int) (b * light));

            paint.setColor(color);


            double distanceToProjectionPlane = 0.75;

            double actualWallHeight = 1.0;

            double projectedWallHeight = (distanceToProjectionPlane * actualWallHeight) / distance;

            double screenUnitsHeight = 1.75;

            float screenWallHeight = (float) (projectedWallHeight * height / screenUnitsHeight);

//            if ((x >= 0) && (x < 100)) {
//                Log.d("Wrrr", "render: projectedWallHeight = " + projectedWallHeight);
//            }

            canvas.drawLine(
                    x, (1.0f * height - screenWallHeight) / 2.0f,
                    x, (1.0f * height + screenWallHeight) / 2.0f,
                    paint
            );
        }
    }

    public void clearCells() {
        for (int i = 0; i < cells; i++) {
            for (int j = 0; j < cells; j++) {
                field[i][j] = false;
            }
        }
    }

    private CastResult traceRay(double angle) {
        double aRayX = Math.cos(angle / 180.0 * Math.PI);
        double aRayY = Math.sin(angle / 180.0 * Math.PI);

        double playerX = gameEngine.getPlayer().getPosition().x();
        double playerY = gameEngine.getPlayer().getPosition().y();

        double x = 0.0;
        double y = 0.0;
        double distance = 0.0;
        int wallType = -1;

        for (Wall wall : gameEngine.getMap().getWalls()) {
            double aWallX = wall.getAx();
            double aWallY = wall.getAy();
            double wallX0 = wall.getX0();
            double wallY0 = wall.getY0();

            double det = (aWallX * (-aRayY)) - (-aRayX * aWallY);

            if (Math.abs(det) < EPS) {
                continue;
            }

            double detT1 = (-aRayY * (playerX - wallX0)) - (-aRayX * (playerY - wallY0));
            double detT2 = (aWallX * (playerY - wallY0)) - (aWallY * (playerX - wallX0));

            double t1 = detT1 / det;
            double t2 = detT2 / det;

            if ((t2 >= 0.0) && (t1 >= 0.0) && (t1 <= 1.0)) {
                double newX = wall.x(t1);
                double newY = wall.y(t1);
                double distanceNew = Math.sqrt(
                        (playerX - newX) * (playerX - newX) + (playerY - newY) * (playerY - newY)
                );
                if ((wallType == -1) || (distanceNew < distance)) {
                    wallType = wall.getTypeId();
                    distance = distanceNew;
                    x = newX;
                    y = newY;
                }
            }
        }

        if (wallType == -1) {
            return null;
        }

        return new CastResult(wallType, x, y);
    }

    private static class CastResult {
        final int blockType;
        final double blockX;
        final double blockY;

        public CastResult(int blockType, double blockX, double blockY) {
            this.blockType = blockType;
            this.blockX = blockX;
            this.blockY = blockY;
        }

        public double distance(double x, double y) {
            return Math.sqrt((x - blockX) * (x - blockX) + (y - blockY) * (y - blockY));
        }
    }
}
