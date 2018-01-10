package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;

public abstract class KeyboardControls extends CommonGame {
    private boolean interrupted = false;
    private boolean keyAPressed = false;
    private boolean keyDPressed = false;
    private boolean keyWPressed = false;
    private boolean keySPressed = false;
    private boolean keyLeftPressed = false;
    private boolean keyRightPressed = false;
    private boolean keyUpPressed = false;

    private final PilotPlayer.Action pilotAction = new PilotPlayer.Action();
    private final GunnerPlayer.Action gunnerAction = new GunnerPlayer.Action();

    protected KeyboardControls(Stage stage) {
        super(stage);
    }

    @Override
    public void start(WorldDescriptor descriptor, ClientMenu menu, GraphicStyleContainer style, PilotPlayer pilot, GunnerPlayer gunner) {
        interrupted = false;
        keyAPressed = false;
        keyDPressed = false;
        keyWPressed = false;
        keySPressed = false;
        keyLeftPressed = false;
        keyRightPressed = false;
        keyUpPressed = false;
        pilotAction.setMove(PilotPlayer.Action.Move.OFF);
        pilotAction.setTurn(PilotPlayer.Action.Turn.NO_TURN);
        gunnerAction.setShoot(false);
        gunnerAction.setTurn(GunnerPlayer.Action.Turn.NO_TURN);
        super.start(descriptor, menu, style, pilot, gunner);
    }

    protected PilotPlayer.Action getPilotAction() {
        return pilotAction;
    }
    protected GunnerPlayer.Action getGunnerAction() {
        return gunnerAction;
    }

    @Override
    protected boolean interrupted() {
        return interrupted;
    }

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

}
