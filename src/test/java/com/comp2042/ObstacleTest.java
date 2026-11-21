
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
}
