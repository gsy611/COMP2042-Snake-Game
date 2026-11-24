package com.comp2042;
import java.util.Random;
import javafx.scene.image.Image;

public class SilverCoin extends Coin {
    private Image image = new Image("resources/views/res/silvercoin.png");

    public SilverCoin(int x, int y, Snake snek, Obstacle obstacle) {
        super(x, y, snek, obstacle);
    }

    public Image getImage() {
        return image;
    }

    public int getPoints() {
        int points = 5;
        return points;
    }
    @Override
    public double applyEffect(Game game, Snake snek, Obstacle obstacle) {
        Random rnd = new Random();

        int obstacleCount =
            rnd.nextInt(game.getWidth() * game.getHeight() / 20) + 3;

        int addedObstacles =
            obstacle.newObstacles(snek, this, obstacleCount);

        game.decMaxScore(addedObstacles);

        return 0;
    }
}
