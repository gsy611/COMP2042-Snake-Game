package com.comp2042;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class GameController {
    private Snake snek;
    private Coin coin;
    private GameView gameView;
    private Game game;
    private Stage stage;
    private Obstacle obstacle;

    public static KeyCode key = KeyCode.LEFT;
    public static KeyCode lastKey = KeyCode.LEFT;
    private boolean keyPressed = false;



    public GameController(Snake snek, Coin coin, Game game, GameView gameView, Obstacle obstacle, Stage stage) {
        this.snek = snek;
        this.coin = coin;
        this.gameView = gameView;
        this.game = game;
        this.stage = stage;

        this.obstacle = obstacle;
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(game.getSpeed());
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        if (snek.getDead()) {
            return;
        } else {

            if (key.equals(KeyCode.LEFT)) {
                snek.move('L');
            } else if (key.equals(KeyCode.RIGHT)) {
                snek.move('R');
            } else if (key.equals(KeyCode.UP)) {
                snek.move('U');
            } else if (key.equals(KeyCode.DOWN)) {
                snek.move('D');
            }

            if (!snek.getDead() && snek.ateCoin(coin)) {
                powerCoin();
                game.incScore();
                if (game.gameWon()) {
                    gameView.drawGrid(coin, snek, obstacle, gameView.gc, false, 0);
                    return;
                }
                newCoin();
            }

            keyPressed = false;

            if (!snek.getDead() || snek.hitSelf()) {
                gameView.drawGrid(coin, snek, obstacle, gameView.gc, false, 0);
            }

            gameView.gameWon();
            gameView.gameOver(snek);
            gameView.updateScoreLabel();
        }
    }));

    
public void handle(KeyEvent event) {
    KeyCode code = event.getCode();

    handleDirectionKey(code);

    if (code == KeyCode.P) {
        pauseGame();
    }

    if (code == KeyCode.TAB) {
        reset();
        gameView.scoreLabel.setText("Score: " + game.getScorePoints());
    }

    if (code == KeyCode.PLUS) {
        changeScreenSize(true);
    }

    if (code == KeyCode.MINUS) {
        changeScreenSize(false);
    }

    if (code == KeyCode.ESCAPE) {
        returnToMainMenu();
    }
}

private void handleDirectionKey(KeyCode code) {
    if (code.isArrowKey()) {
        animation.play();
    }

    if (valKey(code) && !keyPressed) {
        lastKey = key;
        key = code;
        keyPressed = true;
        gameView.turnDir(snek);
    }
}

private void pauseGame() {
    animation.stop();
}

private void changeScreenSize(boolean fullscreen) {
    gameView.scaleToFullscreen(fullscreen, stage, obstacle, snek, coin);

    if (fullscreen) {
        gameView.gameOverLabel.setStyle("-fx-font-size: 50;");
        gameView.gameWonLabel.setStyle("-fx-font-size: 50;");
        gameView.scoreLabel.setStyle("-fx-font-size: 35;");
    } else {
        gameView.gameOverLabel.setStyle("-fx-font-size: 15;");
        gameView.gameWonLabel.setStyle("-fx-font-size: 15;");
        gameView.scoreLabel.setStyle("-fx-font-size: 15;");
    }
}

private void returnToMainMenu() {
    MainMenuController mainMenuController = new MainMenuController();
    mainMenuController.getViewManager().getPrimaryStage().show();
    mainMenuController.getViewManager().getGameStage().close();
}


    private boolean valKey(KeyCode key) {
    if (!key.isArrowKey()) {
        return false;
    }
    if (key == KeyCode.LEFT) {
        return this.key != KeyCode.RIGHT;
    }
    if (key == KeyCode.RIGHT) {
        return this.key != KeyCode.LEFT;
    }
    if (key == KeyCode.UP) {
        return this.key != KeyCode.DOWN;
    }
    if (key == KeyCode.DOWN) {
        return this.key != KeyCode.UP;
    }
    return false;
}

    private void reset() {

    animation.jumpTo(Duration.ZERO);
    animation.stop();

    // Clear obstacles from the previous game first.
    obstacle.reset();

    // Create new game objects.
    snek = new Snake(game.getWidth(), game.getHeight(), game.isNoBorder(), obstacle);
    coin = new Coin(game.getWidth(), game.getHeight(), snek, obstacle);
    animation.setRate(game.getSpeed());
    key = KeyCode.LEFT;
    keyPressed = false;
    game.resetScore();

    gameView.gameWon();
    gameView.gameOver(snek);
    gameView.updateScoreLabel();

    gameView.drawGrid(coin, snek, obstacle, gameView.gc, false, 0);
}

    
private void powerCoin() {
    animation.setRate(game.getSpeed());
    snek.ignoreBorders(false);

    if (coin instanceof RainbowCoin) {
        handleRainbowCoin((RainbowCoin) coin);

    } else if (coin instanceof SilverCoin) {
        handleSilverCoin();

    } else if (coin instanceof BlueCoin) {
        handleBlueCoin();
    }

    game.addPoints(coin.getPoints());
}

private void handleRainbowCoin(RainbowCoin rainbowCoin) {
    animation.setRate(game.getSpeed() + rainbowCoin.getSpeedBonus());
}

private void handleSilverCoin() {
    Random rnd = new Random();

    int obstacleCount =
        rnd.nextInt(game.getWidth() * game.getHeight() / 20) + 3;

    int addedObstacles =
        obstacle.newObstacles(snek, coin, obstacleCount);

    game.decMaxScore(addedObstacles);
}

private void handleBlueCoin() {
    snek.ignoreBorders(true);
}

    private void newCoin() {
        if (game.isPowerUp()) {
            Random rnd = new Random();
            int ran = rnd.nextInt(12);
            if (ran == 1) {
                coin = new GoldCoin(game.getWidth(), game.getHeight(), snek, obstacle);
            } else if (ran == 2) {
                coin = new RainbowCoin(game.getWidth(), game.getHeight(), snek, obstacle);
            } else if (ran == 3) {
                coin = new BlueCoin(game.getWidth(), game.getHeight(), snek, obstacle);
            } else if (ran == 4 && obstacle.getObstacles().size() == 0) {
                coin = new SilverCoin(game.getWidth(), game.getHeight(), snek, obstacle);
            } else {
                coin = new Coin(game.getWidth(), game.getHeight(), snek, obstacle);
            }
        } else {
            coin = new Coin(game.getWidth(), game.getHeight(), snek, obstacle);
        }
    }
}
