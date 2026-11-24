
package com.comp2042;

public class BlueCoin extends Coin {

    public BlueCoin(int x, int y, Snake snek, Obstacle obstacle) {
        super(x, y, snek, obstacle, "resources/views/res/bluecoin.png");
    }

    @Override
    public double applyEffect(Game game, Snake snek, Obstacle obstacle) {
        snek.ignoreBorders(true);
        return 0;
    }
}
