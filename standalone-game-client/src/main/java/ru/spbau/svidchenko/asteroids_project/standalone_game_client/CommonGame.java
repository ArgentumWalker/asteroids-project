package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app.StandaloneAppGameExecutor;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app.StandaloneClientInterface;

public abstract class CommonGame {
    protected abstract EventHandler<KeyEvent> getOnKeyPressHandler();
    protected abstract EventHandler<KeyEvent> getOnKeyReleaseHandler();
    protected abstract EventHandler<MouseEvent> getOnMouseMovedHandler();
    protected abstract EventHandler<MouseEvent> getOnMouseClickHandler();
    protected abstract Scene initScene();
    protected abstract AnimationTimer buildAnimationTimer();
    protected abstract boolean interrupted();

    protected final PilotPlayer.Action pilotAction = new PilotPlayer.Action();
    protected final GunnerPlayer.Action gunnerAction = new GunnerPlayer.Action();
    protected final Scene scene;
    protected final Stage stage;
    protected ShipCrew currentShipCrew;
    protected GraphicStyleContainer style;

    private ClientMenu menu;
    private AnimationTimer animationTimer;

    protected CommonGame(Stage stage) {
        this.stage = stage;
        scene = initScene();
        initEventHandler();
    }

    public void start(WorldDescriptor descriptor, ClientMenu menu, int pilotId, int gunnerId, GraphicStyleContainer style) {
        this.menu = menu;
        this.style = style;
        animationTimer = buildAnimationTimer();
        currentShipCrew = new ShipCrew(new Pilot(pilotId), new Gunner(gunnerId));
        descriptor.players.add(currentShipCrew);
        new Thread(new StandaloneAppGameExecutor(descriptor, Constants.TURNS_IN_GAME, new Client())).start();
        stage.setScene(scene);
    }

    private void initEventHandler() {
        scene.setOnKeyPressed(getOnKeyPressHandler());
        scene.setOnKeyReleased(getOnKeyReleaseHandler());
        scene.setOnMouseMoved(getOnMouseMovedHandler());
        scene.setOnMouseClicked(getOnMouseClickHandler());
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
        protected Action chooseAction() {
            return gunnerAction;
        }
    }

    private class Pilot extends PilotPlayer {
        public Pilot(int id) {
            super(id);
        }

        @Override
        protected Action chooseAction() {
            return pilotAction;
        }
    }
}
