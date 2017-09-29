package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

import java.util.HashMap;

public abstract class PilotPlayer extends Player {
    private Ship.Vehicle vehicle;

    public PilotPlayer(long id) {
        super(id);
    }

    protected abstract Action chooseAction();

    public final void setVehicle(Ship.Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public final void makeAction() {
        Action action = chooseAction();
        switch (action.turn) {
            case LEFT: {
                vehicle.turnLeft();
                break;
            }
            case RIGHT: {
                vehicle.turnRight();
                break;
            }
        }
        switch (action.move) {
            case FORWARD: {
                vehicle.moveForward();
                break;
            }
            case BACKWARD: {
                vehicle.moveBackward();
                break;
            }
            case OFF: {
                vehicle.stop();
                break;
            }
        }
    }

    protected final static class Action {
        public enum Turn {
            LEFT,
            RIGHT,
            NO_TURN
        }
        public enum Move {
            FORWARD,
            BACKWARD,
            OFF
        }
        private Turn turn = Turn.NO_TURN;
        private Move move = Move.OFF;

        public void setTurn(@NotNull Turn turn) {
            this.turn = turn;
        }

        public void setMove(@NotNull Move move) {
            this.move = move;
        }
    }
}
