
package com.comp2042;

import java.util.Random;

public class SilverCoin extends Coin {

    public SilverCoin(int x, int y, Snake snek, Obstacle obstacle) {
        super(x, y, snek, obstacle, "resources/views/res/silvercoin.png");
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
