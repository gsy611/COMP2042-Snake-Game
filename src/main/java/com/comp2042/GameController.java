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

    private KeyCode key = KeyCode.LEFT;
    private KeyCode lastKey = KeyCode.LEFT;
    private boolean keyPressed = false;
    private boolean paused = false;


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
        stopAnimation();
        gameView.gameOver(snek);
        return;
    }

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
            stopAnimation();
        } else {
            newCoin();
        }
    }

    keyPressed = false;

    if (snek.getDead()) {
        stopAnimation();
    }

    gameView.drawGrid(coin, snek, obstacle, gameView.gc, false, 0);

    gameView.gameWon();
    gameView.gameOver(snek);
    gameView.updateScoreLabel();
}));
    private void stopAnimation() {
    animation.stop();
}
    
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

    // Ignore direction input while paused or after the game ends.
    if (paused || snek.getDead() || game.gameWon()) {
        return;
    }

    if (code.isArrowKey()) {
        animation.play();
    }

    if (valKey(code) && !keyPressed) {
        lastKey = key;
        key = code;
        keyPressed = true;

        gameView.turnDir(snek, lastKey, key);
    }
}

private void pauseGame() {

    if (paused) {
        // Resume the game.
        paused = false;
        animation.play();

    } else if (animation.getStatus() == Animation.Status.RUNNING) {
        // Pause the game.
        animation.pause();
        paused = true;
    }
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

    animation.stop();
    animation.jumpTo(Duration.ZERO);

    paused = false;

    obstacle.reset();

    // Create new game objects.
    snek = new Snake(game.getWidth(), game.getHeight(), game.isNoBorder(), obstacle);
    coin = new Coin(game.getWidth(), game.getHeight(), snek, obstacle);
    animation.setRate(game.getSpeed());
    key = KeyCode.LEFT;
    lastKey = KeyCode.LEFT;
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

    if (!Coin.hasAvailablePosition(
            game.getWidth(), game.getHeight(), snek, obstacle)) {

        game.finishGame();
        stopAnimation();
        return;
    }

    coin = createCoin();
}

private Coin createCoin() {

    // Power-ups are disabled: create a normal coin
    if (!game.isPowerUp()) {
        return createNormalCoin();
    }

    Random rnd = new Random();
    int ran = rnd.nextInt(12);

    return switch (ran) {
        case 1 -> new GoldCoin(
                game.getWidth(), game.getHeight(), snek, obstacle);

        case 2 -> new RainbowCoin(
                game.getWidth(), game.getHeight(), snek, obstacle);

        case 3 -> new BlueCoin(
                game.getWidth(), game.getHeight(), snek, obstacle);

        case 4 -> obstacle.getObstacles().isEmpty()
                ? new SilverCoin(game.getWidth(), game.getHeight(), snek, obstacle)
                : createNormalCoin();

        default -> createNormalCoin();
    };
}

private Coin createNormalCoin() {
    return new Coin(game.getWidth(), game.getHeight(), snek, obstacle);
}

    


}
