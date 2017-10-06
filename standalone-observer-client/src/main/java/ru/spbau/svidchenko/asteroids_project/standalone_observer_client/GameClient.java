package ru.spbau.svidchenko.asteroids_project.standalone_observer_client;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.Game;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app.StandaloneAppGameExecutor;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app.StandaloneClientInterface;

import java.util.List;

public class GameClient {
    private final GraphicsContext graphicsContext;
    private final Stage stage;
    private final Scene scene;
    private Game game;
    private AnimationTimer animationTimer;
    private ObserverMenu observerMenu;
    private List<ShipCrew> crews;
    private int crewId = 0;
    private boolean interrupted = false;

    public GameClient(Stage stage) {
        this.stage = stage;
        Group root = new Group();
        scene = new Scene(root);
        Canvas canvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WINDOW_HALF_HEIGHT_PX * 2);
        graphicsContext = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        initEventHandler();
    }

    public void start(WorldDescriptor descriptor, GraphicStyleContainer style, ObserverMenu menu) {
        crews = descriptor.players;
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicUtils.drawGameBackground(graphicsContext, crews.get(crewId).getMembers().first().getWorldModel(), style);
                GraphicUtils.drawWorld(graphicsContext, crews.get(crewId).getMembers().first().getWorldModel(), style);
                GraphicUtils.drawUi(graphicsContext, crews.get(crewId).getMembers().first().getWorldModel(), style, crews.get(crewId));
            }
        };

        interrupted = false;
        StandaloneAppGameExecutor standaloneAppGameExecutor = new StandaloneAppGameExecutor(
                descriptor, Constants.TURNS_IN_GAME, new Client());
        new Thread(standaloneAppGameExecutor).start();
        stage.setScene(scene);
    }

    public void setObserverMenu(ObserverMenu observerMenu) {
        this.observerMenu = observerMenu;
    }

    private void initEventHandler() {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT: {
                    crewId = (crews.size() + crewId + 1) % crews.size();
                    break;
                }
                case RIGHT: {
                    crewId = (crewId + 1) % crews.size();
                    break;
                }
                case ESCAPE: {
                    interrupted = true;
                }
            }
        });
    }

    private class Client implements StandaloneClientInterface {

        @Override
        public void onGameStart() {
            animationTimer.start();
        }

        @Override
        public void onGameEnd() {
            animationTimer.stop();
            Platform.runLater(() -> observerMenu.start());
        }

        @Override
        public boolean isGameInterrupted() {
            return interrupted;
        }
    }
}
