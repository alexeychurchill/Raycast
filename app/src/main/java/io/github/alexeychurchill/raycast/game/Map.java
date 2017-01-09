package io.github.alexeychurchill.raycast.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Map class
 */

public class Map {
    private List<Wall> walls = new LinkedList<>();

    public Map() {
        walls.addAll(Arrays.asList(
                new Wall(0.0, 0.0, 5.0, 0.0, 1),
                new Wall(5.0, 0.0, 5.0, 5.0, 2),
                new Wall(5.0, 5.0, 4.0, 5.0, 3),
                new Wall(4.0, 5.0, 4.0, 3.0, 3),
                new Wall(4.0, 3.0, 2.0, 3.0, 3),
                new Wall(2.0, 3.0, 2.0, 5.0, 3),
                new Wall(2.0, 5.0, 0.0, 5.0, 3),
                new Wall(0.0, 5.0, 0.0, 0.0, 4)
        ));
    }

    public List<Wall> getWalls() {
        return walls;
    }
}
