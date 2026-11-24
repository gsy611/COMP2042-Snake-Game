
package com.comp2042;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HighScoreManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReturnZeroWhenFileDoesNotExist() throws IOException {
        Path file = tempDir.resolve("highscore.txt");
        HighScoreManager manager = new HighScoreManager(file);

        assertEquals(0, manager.loadHighScore());
    }

    @Test
    void shouldSaveFirstHighScore() throws IOException {
        Path file = tempDir.resolve("highscore.txt");
        HighScoreManager manager = new HighScoreManager(file);

        assertTrue(manager.updateHighScore(100));
        assertEquals(100, manager.loadHighScore());
    }

    @Test
    void shouldUpdateWhenNewScoreIsHigher() throws IOException {
        Path file = tempDir.resolve("highscore.txt");
        HighScoreManager manager = new HighScoreManager(file);

        manager.updateHighScore(100);

        assertTrue(manager.updateHighScore(150));
        assertEquals(150, manager.loadHighScore());
    }

    @Test
    void shouldNotUpdateWhenNewScoreIsLower() throws IOException {
        Path file = tempDir.resolve("highscore.txt");
        HighScoreManager manager = new HighScoreManager(file);

        manager.updateHighScore(100);

        assertFalse(manager.updateHighScore(80));
        assertEquals(100, manager.loadHighScore());
    }

    @Test
    void shouldNotUpdateWhenScoresAreEqual() throws IOException {
        Path file = tempDir.resolve("highscore.txt");
        HighScoreManager manager = new HighScoreManager(file);

        manager.updateHighScore(100);

        assertFalse(manager.updateHighScore(100));
        assertEquals(100, manager.loadHighScore());
    }

    @Test
    void shouldKeepHighScoreAfterCreatingNewManager() throws IOException {
        Path file = tempDir.resolve("highscore.txt");

        HighScoreManager firstManager = new HighScoreManager(file);
        firstManager.updateHighScore(200);

        HighScoreManager secondManager = new HighScoreManager(file);

        assertEquals(200, secondManager.loadHighScore());
    }

    @Test
    void shouldReturnZeroWhenFileContainsInvalidData() throws IOException {
        Path file = tempDir.resolve("highscore.txt");
        Files.writeString(file, "invalid");

        HighScoreManager manager = new HighScoreManager(file);

        assertEquals(0, manager.loadHighScore());
    }

    @Test
    void shouldRejectNegativeScore() {
        Path file = tempDir.resolve("highscore.txt");
        HighScoreManager manager = new HighScoreManager(file);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.updateHighScore(-10)
        );
    }
}
