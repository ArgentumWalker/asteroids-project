package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.WorldModel;
import ru.spbau.svidchenko.asteroids_project.graphics_common.Animation;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app.StandaloneAppGameExecutor;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app.StandaloneClientInterface;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class CommonGame {
    protected abstract EventHandler<KeyEvent> getOnKeyPressHandler();
    protected abstract EventHandler<KeyEvent> getOnKeyReleaseHandler();
    protected abstract EventHandler<MouseEvent> getOnMouseMovedHandler();
    protected abstract EventHandler<MouseEvent> getOnMouseClickHandler();
    protected abstract Scene initScene();
    protected abstract void onAnimationTimerAction();
    protected abstract boolean interrupted();
    protected abstract PilotPlayer.Action getPilotAction();
    protected abstract GunnerPlayer.Action getGunnerAction();

    protected final Scene scene;
    protected final Stage stage;
    protected ShipCrew currentShipCrew;
    protected GraphicStyleContainer style;

    private ClientMenu menu;
    private AnimationTimer animationTimer;
    private StandaloneAppGameExecutor gameExecutor;
    private Consumer<Long> scoreLogFunction = null;
    protected Set<Pair<Animation, Point>> animations = new HashSet<>();

    protected CommonGame(Stage stage) {
        this.stage = stage;
        scene = initScene();
    }

    public void start(WorldDescriptor descriptor,
                      ClientMenu menu,
                      GraphicStyleContainer style,
                      PilotPlayer pilot,
                      GunnerPlayer gunner
    ) {
        currentShipCrew = new ShipCrew(pilot, gunner);
        startCommon(descriptor, menu, style);
    }

    public void setScoreLogFunction(Consumer<Long> scoreLogFunction) {
        this.scoreLogFunction = scoreLogFunction;
    }

    public GunnerPlayer getGunner(long id) {
        return new Gunner(id);
    }

    public PilotPlayer getPilot(long id) {
        return new Pilot(id);
    }

    private void startCommon(WorldDescriptor descriptor, ClientMenu menu, GraphicStyleContainer style) {
        this.menu = menu;
        this.style = style;
        animationTimer = buildAnimationTimer();
        descriptor.players.add(currentShipCrew);
        gameExecutor = new StandaloneAppGameExecutor(descriptor, Constants.TURNS_IN_GAME, new Client());
        new Thread(gameExecutor).start();
        initEventHandler();
        stage.setScene(scene);
    }

    private AnimationTimer buildAnimationTimer() {
        return new AnimationTimer() {
            private long lastTurnHandle = 0;

            @Override
            public void handle(long now) {
                Set<Pair<WorldModel.Event, Long>> eventsAndTurns = gameExecutor.getEventsAndTurns();
                long currentTurn = gameExecutor.getTurnsPassed();
                Set<Pair<Animation, Point>> removed = new HashSet<>();
                for (Pair<Animation, Point> animation : animations) {
                    animation.first().passTurns(currentTurn - lastTurnHandle);
                    if (animation.first().getLeftAnimationTurns() <= 0) {
                        removed.add(animation);
                    }
                }
                animations.removeAll(removed);
                eventsAndTurns.forEach(pair -> {
                        Animation animation = style.getAnimationFor(pair.first());
                        if (animation != null) {
                            animation.passTurns(currentTurn - pair.second());
                            animations.add(Pair.of(animation, pair.first().position));
                        }
                    });
                lastTurnHandle = currentTurn;
                onAnimationTimerAction();
            }
        };
    }

    private void initEventHandler() {
        scene.setOnKeyPressed(getOnKeyPressHandler());
        scene.setOnKeyReleased(getOnKeyReleaseHandler());
        scene.setOnMouseMoved(getOnMouseMovedHandler());
        scene.setOnMouseClicked(getOnMouseClickHandler());
        stage.setOnCloseRequest(event -> gameExecutor.interrupt());
    }

    private class Client implements StandaloneClientInterface {

        @Override
        public void onGameStart() {
            animationTimer.start();
        }

        @Override
        public void onGameEnd() {
            animationTimer.stop();
            Platform.runLater(() -> menu.start());
            if (scoreLogFunction != null) {
                scoreLogFunction.accept(currentShipCrew.getScore());
            }
        }

        @Override
        public void onGameInterrupt() {
            animationTimer.stop();
            Platform.runLater(() -> menu.start());
        }

        @Override
        public boolean isGameInterrupted() {
            return interrupted();
        }
    }

    private class Gunner extends GunnerPlayer {
        public Gunner(long id) {
            super(id);
        }

        @Override
        public Action chooseAction() {
            return getGunnerAction();
        }
    }

    private class Pilot extends PilotPlayer {
        public Pilot(long id) {
            super(id);
        }

        @Override
        protected Action chooseAction() {
            return getPilotAction();
        }
    }
}
