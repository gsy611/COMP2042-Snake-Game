
package com.comp2042;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testInitialScore() {
        Game game = new Game(20, 20, true, 'N');

        assertEquals(0, game.getScorePoints());
    }

    @Test
    void testAddPoints() {
        Game game = new Game(20, 20, true, 'N');

        game.addPoints(10);
        game.addPoints(5);

        assertEquals(15, game.getScorePoints());
    }

    @Test
    void testResetScore() {
        Game game = new Game(20, 20, true, 'N');

        game.addPoints(50);
        game.incScore();

        game.resetScore();

        assertEquals(0, game.getScorePoints());
        assertFalse(game.gameWon());
    }

    @Test
    void testGameWon() {
        Game game = new Game(2, 2, true, 'N');

        game.decMaxScore(1);

        game.incScore();

        assertTrue(game.gameWon());
    }

    @Test
    void testDifficultySettings() {
        Game easy = new Game(20, 20, true, 'E');
        Game normal = new Game(20, 20, true, 'N');
        Game hard = new Game(20, 20, true, 'H');

        assertEquals(8.5, easy.getSpeed());
        assertTrue(easy.isNoBorder());

        assertEquals(8.5, normal.getSpeed());
        assertFalse(normal.isNoBorder());

        assertEquals(13.0, hard.getSpeed());
        assertFalse(hard.isNoBorder());
    }

    @Test
    void testMultiplier() {
        Game smallMap = new Game(20, 20, true, 'N');
        Game largeMap = new Game(20, 60, true, 'N');

        assertEquals(20.0, smallMap.getMultiplier());
        assertEquals(10.0, largeMap.getMultiplier());
    }
}
