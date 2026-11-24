package com.comp2042;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Coin {
    private Point pos;
    private final Image image;
    public static boolean hasAvailablePosition(
        int x, int y, Snake snek, Obstacle obstacle) {

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Point candidate = new Point(i, j);

                if (!snek.getHead().equals(candidate)
                        && !snek.getBody().contains(candidate)
                        && !obstacle.getObstacles().contains(candidate)) {
                    return true;
                }
            }
        }

        return false;
    }
   public Coin(int x, int y, Snake snek, Obstacle obstacle) {
    this(x, y, snek, obstacle, "resources/views/res/redcoin.png");
}

protected Coin(
        int x, int y, Snake snek, Obstacle obstacle, String imagePath) {

    if (x <= 0 || y <= 0) {
        throw new IllegalArgumentException("Grid dimensions must be positive.");
    }

    this.image = new Image(imagePath);

    ArrayList<Point> availablePositions = new ArrayList<>();

    for (int i = 0; i < x; i++) {
        for (int j = 0; j < y; j++) {
            Point candidate = new Point(i, j);

            if (!snek.getHead().equals(candidate)
                    && !snek.getBody().contains(candidate)
                    && !obstacle.getObstacles().contains(candidate)) {
                availablePositions.add(candidate);
            }
        }
    }

    if (availablePositions.isEmpty()) {
        throw new IllegalStateException("No available position for a coin.");
    }

    Random rnd = new Random();
    pos = availablePositions.get(rnd.nextInt(availablePositions.size()));
}

    public Point getPos() {
        return new Point(pos);
    }

    public int getPoints() {
        return 5;
    }
    public double applyEffect(Game game, Snake snek, Obstacle obstacle) {
        return 0;
    }
    public Image getImage() {
        return image;
    }
}
