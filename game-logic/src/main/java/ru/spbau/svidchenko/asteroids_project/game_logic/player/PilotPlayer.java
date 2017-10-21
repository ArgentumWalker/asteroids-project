package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import ru.spbau.svidchenko.asteroids_project.game_logic.world.Entity;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

import java.util.HashMap;
import java.util.List;

public abstract class PilotPlayer extends Player {
    protected Ship.Vehicle vehicle;

    public PilotPlayer(long id) {
        super(id);
    }

    protected abstract Action chooseAction();

    public final void setVehicle(Ship.Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public final List<Entity> makeAction() {
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
        return null;
    }

    public final static class Action {
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

        public void setTurn(Turn turn) {
            this.turn = turn;
        }

        public void setMove(Move move) {
            this.move = move;
        }
    }
}
