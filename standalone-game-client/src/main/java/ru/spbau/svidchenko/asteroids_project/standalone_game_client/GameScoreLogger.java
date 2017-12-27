package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.function.Consumer;

public class GameScoreLogger implements Consumer<Long> {
    private final static String STATISTIC_DIRECTORY = "statistic";
    private final String logFileName;

    public GameScoreLogger(String logName) {
        logFileName = STATISTIC_DIRECTORY + "/" + logName;
    }

    @Override
    public void accept(Long score) {
        try {
            File file = new File(logFileName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (PrintStream outputStream = new PrintStream(new FileOutputStream(file, true))) {
                outputStream.println(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
