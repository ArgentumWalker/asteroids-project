package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.io.Serializable;
import java.util.function.BiFunction;

public final class PowerFunctions {
    public static PowerFunction getSquaredPowerFunction(double coef) {
        return (s, r) -> {
            double dst = s.getPosition().worldDistanceTo(r.getPosition());
            Point p = r.getPosition().clone().add(s.getPosition().getInverse()).checkWorldBounds().mult(1 / dst);
            double k = coef / (dst * dst); //+one to normalize
            return p.getInverse().mult(k);
        };
    }

    public static PowerFunction getSquaredMinusPowerFunction(double coef) {
        return (s, r) -> {
            double dst = s.getPosition().worldDistanceTo(r.getPosition());
            Point p = r.getPosition().clone().add(s.getPosition().getInverse()).checkWorldBounds().mult(1 / dst);
            dst -= (Constants.SHIP_RADIUS + Constants.STONE_RADIUS - 2.);
            double k = coef / (dst * dst); //+one to normalize
            return p.getInverse().mult(k);
        };
    }

    public static PowerFunction getCubedMinusPowerFunction(double coef) {
        return (s, r) -> {
            double dst = s.getPosition().worldDistanceTo(r.getPosition());
            Point p = r.getPosition().clone().add(s.getPosition().getInverse()).checkWorldBounds().mult(1 / dst);
            dst -= (Constants.SHIP_RADIUS + Constants.STONE_RADIUS - 2.);
            double k = coef / (dst * dst * dst); //+one to normalize
            return p.getInverse().mult(k);
        };
    }

    private interface PowerFunction extends BiFunction<Ship.Relative, Stone.Relative, Point>, Serializable {}
}
