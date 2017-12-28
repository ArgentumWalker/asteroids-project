package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import ru.spbau.svidchenko.asteroids_project.game_logic.Game;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.WorldModel;

public abstract class BaseGameExecutor implements Runnable {
    protected Game game;
    protected WorldDescriptor worldDescriptor;
    protected long durationInTurns;
    protected long turnsPassed = 0;
    private boolean interrupted = false;

    protected BaseGameExecutor(WorldDescriptor worldDescriptor, long durationInTurns) {
        this.worldDescriptor = worldDescriptor;
        game = new Game(worldDescriptor);
        this.durationInTurns = durationInTurns;
    }

    @Override
    public final void run() {
        onGameStart();
        while (turnsPassed < durationInTurns && !gameInterrupted()) {
            makeTurn();
            turnsPassed++;
        }
        if (durationInTurns == turnsPassed) {
            onGameEnd();
        } else {
            onGameInterrupt();
        }
    }

    public void interrupt() {
        interrupted = true;
    }

    public long getTurnsPassed() {
        return turnsPassed;
    }

    protected abstract void makeTurn();

    protected boolean gameInterrupted() {
        return interrupted;
    }

    protected void onGameStart() {}

    protected void onGameEnd() {}

    protected void onGameInterrupt() {}
}
