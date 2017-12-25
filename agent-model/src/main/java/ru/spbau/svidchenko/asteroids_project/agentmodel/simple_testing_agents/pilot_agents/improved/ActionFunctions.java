package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.util.function.Function;

public final class ActionFunctions {
    public static Function<Point, PilotPlayer.Action> getAngleActionFunction(double xConst, double alpha) {
        return (p) -> {
            PilotPlayer.Action result = new PilotPlayer.Action();
            double angle = p.getAngle();
            if (angle > alpha) {
                result.setTurn(PilotPlayer.Action.Turn.LEFT);
            }
            if (angle < -alpha) {
                result.setTurn(PilotPlayer.Action.Turn.RIGHT);
            }
            if (p.getX() > xConst) {
                result.setMove(PilotPlayer.Action.Move.FORWARD);
            }
            if (p.getX() < -xConst) {
                result.setMove(PilotPlayer.Action.Move.BACKWARD);
            }
            return result;
        };
    }

    public static Function<Point, PilotPlayer.Action> getSinActionFunction(double xConst, double alpha) {
        return (p) -> {
            PilotPlayer.Action result = new PilotPlayer.Action();
            double sin = Math.sin(p.getAngle());
            if (sin > alpha) {
                result.setTurn(PilotPlayer.Action.Turn.RIGHT);
            }
            if (sin < -alpha) {
                result.setTurn(PilotPlayer.Action.Turn.LEFT);
            }
            if (p.getX() > xConst) {
                result.setMove(PilotPlayer.Action.Move.FORWARD);
            }
            if (p.getX() < -xConst) {
                result.setMove(PilotPlayer.Action.Move.BACKWARD);
            }
            return result;
        };
    }

    public static Function<Point, PilotPlayer.Action> getSinForwardActionFunction(double xConst, double alpha) {
        return (p) -> {
            PilotPlayer.Action result = new PilotPlayer.Action();
            double sin = Math.sin(p.getAngle());
            if (sin > alpha || (sin > 0 && p.getX() < 0 )) {
                result.setTurn(PilotPlayer.Action.Turn.RIGHT);
            }
            if (sin < -alpha || (sin < 0 && p.getX() < 0 )) {
                result.setTurn(PilotPlayer.Action.Turn.LEFT);
            }
            if (p.getX() > xConst) {
                result.setMove(PilotPlayer.Action.Move.FORWARD);
            }
            if (p.getX() < -xConst) {
                result.setMove(PilotPlayer.Action.Move.BACKWARD);
            }
            return result;
        };
    }
}
