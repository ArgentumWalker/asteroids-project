package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

public class ShootClosestToDirectionGunnerAgent extends GunnerAgent {
    private final double c;

    public ShootClosestToDirectionGunnerAgent(double c) {
        this.c = Math.abs(c);
    }

    @Override
    public String getName() {
        return "ClosestShoot_" + c;
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new GunnerPlayer(id) {
            private final Point ZERO = Point.with(0, 0);
            private static final int CHOOSE_DELAY_REMAINING = 3;
            private int chooseDelayRemaining = 0;
            private Action action = new Action();

            @Override
            public Action chooseAction() {
                action.setShoot(true);
                if (chooseDelayRemaining == 0) {
                    action.setTurn(chooseTurn());
                    chooseDelayRemaining = CHOOSE_DELAY_REMAINING;
                } else {
                    chooseDelayRemaining--;
                }
                return action;
            }

            private Action.Turn chooseTurn() {
                Ship.Relative myShipRelative = null;
                for (EntityRelative relative : worldModel.getRelatives()) {
                    if (relative.getEntity() instanceof Ship) {
                        myShipRelative = (Ship.Relative) relative;
                    }
                }
                EntityRelative closest = null;
                double closestDistance = Constants.WORLD_HALF_WIDTH * 4;
                for (EntityRelative relative : worldModel.getRelatives()) {
                    if (relative.getEntity() instanceof Stone) {
                        double cos = Math.cos(relative.getPosition().getAngle() - myShipRelative.getVehicleOrientation());
                        double distance = relative.getPosition().worldDistanceTo(ZERO) * Math.abs(c - cos * cos * cos);
                        if (closest == null || relative.getPosition().worldDistanceTo(ZERO) < closestDistance) {
                            closest = relative;
                            closestDistance = distance;
                        }
                    }
                }
                if (closest == null) {
                    return Action.Turn.NO_TURN;
                }
                double angle = closest.getPosition().getAngle();
                if (angle > Constants.EPS * 0.001) {
                    return Action.Turn.RIGHT;
                }
                if (angle < -Constants.EPS * 0.001) {
                    return Action.Turn.LEFT;
                }
                return  Action.Turn.NO_TURN;
            }
        };
    }
}
