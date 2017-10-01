package ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.BaseGameExecutor;

import java.util.concurrent.TimeUnit;

public class StandaloneAppGameExecutor extends BaseGameExecutor {
    protected StandaloneClientInterface client;
    protected boolean interrupted = false;

    public StandaloneAppGameExecutor(
            WorldDescriptor worldDescriptor,
            long durationInTurns,
            StandaloneClientInterface client
    ) {
        super(worldDescriptor, durationInTurns);
        this.client = client;
    }

    @Override
    protected void makeTurn() {
        try {
            game.nextTurn();
            GraphicUtils.drawWorld(client.getCanvas(), client.getWorldModel(), client.getGraphicStyle());
            TimeUnit.MILLISECONDS.sleep(Constants.MILLIS_PER_TURN);
        } catch (InterruptedException e) {
            interrupted = true;
        }
    }

    @Override
    protected void onGameEnd() {
        super.onGameEnd();
        client.onGameEnd();
    }

    @Override
    protected void onGameStart() {
        super.onGameStart();
        client.onGameStart();
    }

    @Override
    protected boolean gameInterrupted() {
        return super.gameInterrupted() || interrupted;
    }
}
