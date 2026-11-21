package com.comp2042;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Obstacle {
    private Color color = Color.BLACK;
    private ArrayList<Point> obstacles = new ArrayList<>();
    private int x;
    private int y;

    public Obstacle(int x, int y) {
       this.x = x;
       this.y = y;
    }

public int newObstacles(Snake snek, Coin coin, int amount) {

    // Do not generate more than 50 obstacles
    if (obstacles.size() >= 50) {
        return 0;
    }

    Random rnd = new Random();

    // Store all available positions
    ArrayList<Point> availablePositions = new ArrayList<>();

    // Check every valid grid position
    for (int i = 0; i < x; i++) {
        for (int j = 0; j < y; j++) {

            Point p = new Point(i, j);

            // Check whether the position is occupied
            if (!p.equals(coin.getPos())
                    && !snek.getBody().contains(p)
                    && !snek.getHead().equals(p)
                    && !obstacles.contains(p)) {

                availablePositions.add(p);
            }
        }
    }

    // Generate obstacles only when positions are available
    int remainingCapacity = 50 - obstacles.size();

    int actualAmount = Math.min(
            amount,
            Math.min(availablePositions.size(), remainingCapacity)
    );

    for (int i = 0; i < actualAmount; i++) {

        // Select a random available position
        int index = rnd.nextInt(availablePositions.size());

        Point selectedPosition = availablePositions.remove(index);

        obstacles.add(selectedPosition);
    }
    return actualAmount;
}

    
    public Color getColor() {
        return color;
    }

    public ArrayList<Point> getObstacles() {
        return obstacles;
    }

    public void reset() {
        obstacles.clear();
    }
}
