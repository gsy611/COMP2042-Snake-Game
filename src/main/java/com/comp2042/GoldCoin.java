
package com.comp2042;

public class GoldCoin extends Coin {

    public GoldCoin(int x, int y, Snake snek, Obstacle obstacle) {
        super(x, y, snek, obstacle, "resources/views/res/goldcoin.png");
    }

    @Override
    public int getPoints() {
        return 15;
    }
}
