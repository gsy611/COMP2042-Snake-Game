
package com.comp2042;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HighScoreManager {

    private final Path filePath;

    public HighScoreManager() {
        this(Path.of(
                System.getProperty("user.home"),
                ".advanced-snake",
                "highscore.txt"
        ));
    }

    public HighScoreManager(Path filePath) {
        this.filePath = filePath;
    }

    public int loadHighScore() throws IOException {
        if (!Files.exists(filePath)) {
            return 0;
        }

        String content = Files.readString(filePath).trim();

        try {
            int highScore = Integer.parseInt(content);
            return Math.max(highScore, 0);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean updateHighScore(int score) throws IOException {
        if (score < 0) {
            throw new IllegalArgumentException(
                    "Score cannot be negative."
            );
        }

        int currentHighScore = loadHighScore();

        if (score > currentHighScore) {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(filePath, String.valueOf(score));
            return true;
        }

        return false;
    }
}

