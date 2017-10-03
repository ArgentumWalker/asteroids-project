package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;

public class OnePlayerGame extends CommonGame {

    private boolean interrupted = false;
    private GraphicsContext pilotContext;
    private GraphicsContext gunnerContext;

    protected OnePlayerGame(Stage stage) {
        super(stage);
    }

    private boolean keyAPressed = false;
    private boolean keyDPressed = false;
    private boolean keyWPressed = false;
    private boolean keySPressed = false;
    private boolean keyLeftPressed = false;
    private boolean keyRightPressed = false;
    private boolean keyUpPressed = false;

    @Override
    protected EventHandler<KeyEvent> getOnKeyPressHandler() {
        return event -> {
            switch (event.getCode()) {
                //Exit
                case ESCAPE: {
                    interrupted = true;
                }
                //Pilot
                case A: {
                    keyAPressed = true;
                    if (keyDPressed) {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.NO_TURN);
                    } else {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.LEFT);
                    }
                    break;
                }
                case D: {
                    keyDPressed = true;
                    if (keyAPressed) {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.NO_TURN);
                    } else {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.RIGHT);
                    }
                    break;
                }
                case W: {
                    keyWPressed = true;
                    if (keySPressed) {
                        pilotAction.setMove(PilotPlayer.Action.Move.OFF);
                    } else {
                        pilotAction.setMove(PilotPlayer.Action.Move.FORWARD);
                    }
                    break;
                }
                case S: {
                    keySPressed = true;
                    if (keyWPressed) {
                        pilotAction.setMove(PilotPlayer.Action.Move.OFF);
                    } else {
                        pilotAction.setMove(PilotPlayer.Action.Move.BACKWARD);
                    }
                    break;
                }
                //Gunner
                case LEFT: {
                    keyLeftPressed = true;
                    if (keyRightPressed) {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.NO_TURN);
                    } else {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.LEFT);
                    }
                    break;
                }
                case RIGHT: {
                    keyRightPressed = true;
                    if (keyLeftPressed) {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.NO_TURN);
                    } else {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.RIGHT);
                    }
                    break;
                }
                case UP: {
                    gunnerAction.setShoot(true);
                    break;
                }
            }
        };
    }

    @Override
    protected EventHandler<KeyEvent> getOnKeyReleaseHandler() {
        return event -> {
            switch (event.getCode()) {
                case A: {
                    keyAPressed = false;
                    if (keyDPressed) {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.RIGHT);
                    } else {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.NO_TURN);
                    }
                    break;
                }
                case D: {
                    keyDPressed = false;
                    if (keyAPressed) {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.LEFT);
                    } else {
                        pilotAction.setTurn(PilotPlayer.Action.Turn.NO_TURN);
                    }
                    break;
                }
                case W: {
                    keyWPressed = false;
                    if (keySPressed) {
                        pilotAction.setMove(PilotPlayer.Action.Move.BACKWARD);
                    } else {
                        pilotAction.setMove(PilotPlayer.Action.Move.OFF);
                    }
                    break;
                }
                case S: {
                    keySPressed = false;
                    if (keyWPressed) {
                        pilotAction.setMove(PilotPlayer.Action.Move.FORWARD);
                    } else {
                        pilotAction.setMove(PilotPlayer.Action.Move.OFF);
                    }
                    break;
                }
                //Gunner
                case LEFT: {
                    keyLeftPressed = false;
                    if (keyRightPressed) {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.RIGHT);
                    } else {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.NO_TURN);
                    }
                    break;
                }
                case RIGHT: {
                    keyRightPressed = false;
                    if (keyLeftPressed) {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.LEFT);
                    } else {
                        gunnerAction.setTurn(GunnerPlayer.Action.Turn.NO_TURN);
                    }
                    break;
                }
                case UP: {
                    gunnerAction.setShoot(false);
                    break;
                }
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> getOnMouseMovedHandler() {
        return event -> {};
    }

    @Override
    protected EventHandler<MouseEvent> getOnMouseClickHandler() {
        return event -> {};
    }

    @Override
    public void start(WorldDescriptor descriptor, ClientMenu menu, int pilotId, int gunnerId, GraphicStyleContainer style) {
        interrupted = false;
        stage.setWidth(Constants.WINDOW_HALF_WIDTH_PX * 2);
        stage.setHeight(Constants.WINDOW_HALF_HEIGHT_PX * 2);
        super.start(descriptor, menu, pilotId, gunnerId, style);
    }

    @Override
    protected Scene initScene() {
        Group root = new Group();

        Canvas pilotCanvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WORLD_HALF_HEIGHT * 2);

        pilotContext = pilotCanvas.getGraphicsContext2D();

        root.getChildren().add(pilotCanvas);
        return new Scene(root);
    }

    @Override
    protected AnimationTimer buildAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicUtils.drawGameBackground(pilotContext, currentShipCrew.getMembers().first().getWorldModel(), style);
                GraphicUtils.drawWorld(pilotContext, currentShipCrew.getMembers().first().getWorldModel(), style);
                GraphicUtils.drawUi(pilotContext, currentShipCrew.getMembers().first().getWorldModel(), style, currentShipCrew);
            }
        };
    }

    @Override
    protected boolean interrupted() {
        return interrupted;
    }
}
