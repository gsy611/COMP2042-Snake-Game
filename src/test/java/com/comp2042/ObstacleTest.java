
package com.comp2042;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {
@BeforeAll
static void startJavaFX() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);

    Platform.startup(latch::countDown);

    assertTrue(
            latch.await(10, TimeUnit.SECONDS),
            "JavaFX failed to initialize"
    );

    Platform.setImplicitExit(false);
}
    @Test
    void testGenerateObstacleCount() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);
        Coin coin = new Coin(20, 20, snake, obstacle);

        int generated = obstacle.newObstacles(snake, coin, 10);

        assertEquals(10, generated);
        assertEquals(10, obstacle.getObstacles().size());
    }

    @Test
    void testNoOverlappingPositions() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);
        Coin coin = new Coin(20, 20, snake, obstacle);

        obstacle.newObstacles(snake, coin, 20);

        for (Point position : obstacle.getObstacles()) {
            assertNotEquals(snake.getHead(), position);
            assertFalse(snake.getBody().contains(position));
            assertNotEquals(coin.getPos(), position);
        }
    }

    @Test
    void testMaximumObstacleLimit() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);
        Coin coin = new Coin(20, 20, snake, obstacle);

        int generated = obstacle.newObstacles(snake, coin, 100);

        assertEquals(50, generated);
        assertEquals(50, obstacle.getObstacles().size());

        int additional = obstacle.newObstacles(snake, coin, 10);

        assertEquals(0, additional);
        assertEquals(50, obstacle.getObstacles().size());
    }

    @Test
    void testNoDuplicateObstacles() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);
        Coin coin = new Coin(20, 20, snake, obstacle);

        obstacle.newObstacles(snake, coin, 30);

        assertEquals(
                obstacle.getObstacles().size(),
                new HashSet<>(obstacle.getObstacles()).size()
        );
    }

    @Test
    void testResetObstacles() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);
        Coin coin = new Coin(20, 20, snake, obstacle);

        obstacle.newObstacles(snake, coin, 10);

        assertEquals(10, obstacle.getObstacles().size());

        obstacle.reset();

        assertTrue(obstacle.getObstacles().isEmpty());
    }
    @Test
    void testCoinPositionWithinGrid() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);

        for (int i = 0; i < 20; i++) {
            Coin coin = new Coin(20, 20, snake, obstacle);
            Point position = coin.getPos();

            assertTrue(position.x >= 0 && position.x < 20);
            assertTrue(position.y >= 0 && position.y < 20);
        }
    }

    @Test
    void testCoinDoesNotOverlapSnake() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);

        for (int i = 0; i < 20; i++) {
            Coin coin = new Coin(20, 20, snake, obstacle);

            assertNotEquals(snake.getHead(), coin.getPos());
            assertFalse(snake.getBody().contains(coin.getPos()));
        }
    }

    @Test
    void testCoinDoesNotOverlapObstacles() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);

        Coin initialCoin = new Coin(20, 20, snake, obstacle);
        obstacle.newObstacles(snake, initialCoin, 30);

        for (int i = 0; i < 20; i++) {
            Coin coin = new Coin(20, 20, snake, obstacle);

            assertFalse(
                    obstacle.getObstacles().contains(coin.getPos())
            );
        }
    }

    @Test
    void testMultipleObstacleGenerations() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);
        Coin coin = new Coin(20, 20, snake, obstacle);

        int firstBatch = obstacle.newObstacles(snake, coin, 10);
        int secondBatch = obstacle.newObstacles(snake, coin, 15);
        int thirdBatch = obstacle.newObstacles(snake, coin, 20);

        assertEquals(10, firstBatch);
        assertEquals(15, secondBatch);
        assertEquals(20, thirdBatch);

        assertEquals(45, obstacle.getObstacles().size());

        assertEquals(
                obstacle.getObstacles().size(),
                new HashSet<>(obstacle.getObstacles()).size()
        );
    }
}
