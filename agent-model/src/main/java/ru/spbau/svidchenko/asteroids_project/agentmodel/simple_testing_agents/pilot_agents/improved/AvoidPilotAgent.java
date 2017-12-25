package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AvoidPilotAgent extends PilotAgent {
    private final BiFunction<Ship.Relative, Stone.Relative, Point> powerFunction;
    private final Point gravityPower;
    private final Function<Point, PilotPlayer.Action> actionFunction;
    private final String additionalName;

    public AvoidPilotAgent(
            BiFunction<Ship.Relative, Stone.Relative, Point> powerFunction,
            Point gravityPower,
            Function<Point, PilotPlayer.Action> actionFunction,
            String additionalName
    ) {
        this.powerFunction = powerFunction;
        this.gravityPower = gravityPower;
        this.actionFunction = actionFunction;
        this.additionalName = additionalName;
    }

    @Override
    public String getName() {
        return "AvoidPilotAgent_" + additionalName;
    }

    @Override
    public PilotPlayer buildPlayer(long id) {
        return new AvoidPilotPlayer(id);
    }

    public class AvoidPilotPlayer extends PilotPlayer {

        public AvoidPilotPlayer(long id) {
            super(id);
        }

        @Override
        protected Action chooseAction() {
            Ship ship = vehicle.getShip();
            final Point[] resultPower = {Point.with(0, 0)};
            Ship.Relative shipRelative = worldModel.getRelatives().stream()
                    .filter(r -> r instanceof Ship.Relative && r.getEntity().equals(ship))
                    .map(r -> (Ship.Relative)r)
                    .findAny().get();
            worldModel.getRelatives().stream().filter(r -> r instanceof Stone.Relative)
                    .map(r -> Pair.of((Stone.Relative)r, r.getPosition().worldDistanceTo(shipRelative.getPosition())))
                    .forEach(r -> resultPower[0] = resultPower[0].add(powerFunction.apply(shipRelative, r.first())));
            return actionFunction.apply(resultPower[0].rotate(shipRelative.getVehicleOrientation()).add(gravityPower));
        }
    }
}
