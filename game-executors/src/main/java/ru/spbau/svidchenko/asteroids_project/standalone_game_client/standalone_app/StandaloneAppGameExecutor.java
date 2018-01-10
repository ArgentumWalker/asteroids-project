package ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.WorldModel;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.BaseGameExecutor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StandaloneAppGameExecutor extends BaseGameExecutor {
    protected StandaloneClientInterface client;
    protected boolean interrupted = false;
    private final Set<Pair<WorldModel.Event, Long>> eventsAndTurns = new HashSet<>();

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
            Set<WorldModel.Event> events = game.getCurrentWorldodel().getCurrentEvents();
            synchronized (eventsAndTurns) {
                eventsAndTurns.addAll(events.stream().map(event -> Pair.of(event, turnsPassed)).collect(Collectors.toList()));
            }
            TimeUnit.MILLISECONDS.sleep(Constants.MILLIS_PER_TURN);
        } catch (InterruptedException e) {
            interrupted = true;
        }
    }

    public Set<Pair<WorldModel.Event, Long>> getEventsAndTurns() {
        HashSet<Pair<WorldModel.Event, Long>> result;
        synchronized (eventsAndTurns) {
            result = new HashSet<>(eventsAndTurns);
            eventsAndTurns.clear();
        }
        return result;
    }

    @Override
    protected void onGameEnd() {
        super.onGameEnd();
        client.onGameEnd();
    }

    @Override
    protected void onGameInterrupt() {
        super.onGameInterrupt();
        client.onGameInterrupt();
    }

    @Override
    protected void onGameStart() {
        super.onGameStart();
        client.onGameStart();
    }

    @Override
    protected boolean gameInterrupted() {
        return super.gameInterrupted() || interrupted || client.isGameInterrupted();
    }
}
