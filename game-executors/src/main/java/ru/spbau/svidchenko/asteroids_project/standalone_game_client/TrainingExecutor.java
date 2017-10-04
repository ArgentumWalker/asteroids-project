package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;

public class TrainingExecutor extends BaseGameExecutor {
    protected boolean interrupted = false;
    protected final Runnable onGameEnd;

    public TrainingExecutor(
            WorldDescriptor worldDescriptor,
            long durationInTurns,
            Runnable onGameEnd
    ) {
        super(worldDescriptor, durationInTurns);
        this.onGameEnd = onGameEnd;
    }

    @Override
    protected void makeTurn() {
        game.nextTurn();
    }

    @Override
    protected void onGameEnd() {
        super.onGameEnd();
        onGameEnd.run();
    }

    @Override
    protected void onGameStart() {
        super.onGameStart();
    }

    @Override
    protected boolean gameInterrupted() {
        return super.gameInterrupted();
    }
}
