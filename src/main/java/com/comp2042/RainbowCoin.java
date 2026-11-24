
package com.comp2042;

public class RainbowCoin extends Coin {

    public RainbowCoin(int x, int y, Snake snek, Obstacle obstacle) {
        super(x, y, snek, obstacle, "resources/views/res/rainbowcoin.png");
    }

    public double getSpeedBonus() {
        return 5;
    }

    @Override
    public double applyEffect(Game game, Snake snek, Obstacle obstacle) {
        return getSpeedBonus();
    }
}
