package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.selective_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class QNetSelectiveGunnerAgent extends GunnerAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final boolean gunnerPowerEnabled;
    private final boolean pilotPowerEnabled;
    private final boolean gunnerPilotPowerEnabled;
    private final boolean gunnerPilotAngleEnabled;
    private final BiFunction<Ship.Relative, Stone.Relative, Point> powerFunction;
    private final SelectiveQNet policy;
    private final List<GunnerAgent> determenisticGunnerAgents;
    private final long chooseTurns;

    private final ConcurrentHashMap<Integer, Long> chooseStatistic = new ConcurrentHashMap<>();

    public QNetSelectiveGunnerAgent(
            boolean gunnerPowerEnabled,
            boolean pilotPowerEnabled,
            boolean gunnerPilotPowerEnabled,
            boolean gunnerPilotAngleEnabled,
            BiFunction<Ship.Relative, Stone.Relative, Point> powerFunction,
            List<GunnerAgent> determenisticGunnerAgents,
            long chooseTurns,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        this.powerFunction = powerFunction;
        this.gunnerPowerEnabled = gunnerPowerEnabled;
        this.pilotPowerEnabled = pilotPowerEnabled;
        this.gunnerPilotAngleEnabled = gunnerPilotAngleEnabled;
        this.determenisticGunnerAgents = determenisticGunnerAgents;
        this.chooseTurns = chooseTurns;
        this.gunnerPilotPowerEnabled = gunnerPilotPowerEnabled && gunnerPowerEnabled && pilotPowerEnabled;
        this.policy = new SelectiveQNet(explorationProbability, alpha, gamma, determenisticGunnerAgents.size(),
                (gunnerPowerEnabled ? 1 : 0) +
                        (pilotPowerEnabled ? 1 : 0) +
                        (gunnerPilotPowerEnabled ? 2 : 0) +
                        (gunnerPilotAngleEnabled ? 1 : 0),
                this::isLearningDisabled);
        for (int i = 0; i < determenisticGunnerAgents.size(); i++) {
            chooseStatistic.put(i, 0L);
        }
    }

    @Override
    public String getName() {
        return "QNetSelectiveGunner_" + id;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        policy.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        policy.setAlpha(alpha);
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new QNetSelectiveGunnerPlayer(id, determenisticGunnerAgents);
    }

    private List<Point> calculatePowers(RelativeWorldModel worldModel, Ship ship) {
        List<Point> result = new ArrayList<>();
        Point gunnerPower = null;
        Point pilotPower = null;
        Ship.Relative shipRelative = worldModel.getRelatives().stream()
                .filter(r -> r instanceof Ship.Relative && r.getEntity().equals(ship))
                .map(r -> (Ship.Relative)r)
                .findAny().get();
        if (gunnerPowerEnabled) {
            gunnerPower = calculateGunnerPower(worldModel, shipRelative);
            result.add(gunnerPower);
        }
        if (pilotPowerEnabled) {
            pilotPower = calculateGunnerPower(worldModel, shipRelative);
            result.add(pilotPower);
        }
        if (gunnerPilotPowerEnabled) {
            result.addAll(calculateGunnerPilotPower(gunnerPower, pilotPower));
        }
        if (gunnerPilotAngleEnabled) {
            result.add(calculateGunnerPilotAngle(shipRelative));
        }
        return result;
    }

    private Point calculateGunnerPower(RelativeWorldModel worldModel, Ship.Relative ship) {
        final Point[] resultPower = {Point.with(0, 0)};
        worldModel.getRelatives().stream().filter(r -> r instanceof Stone.Relative)
                .forEach(r -> {
                    resultPower[0].add(powerFunction.apply(ship, (Stone.Relative)r).rotate(ship.getWeaponOrientation()));
                });
        return resultPower[0];
    }

    private Point calculatePilotPower(RelativeWorldModel worldModel, Ship.Relative ship) {
        final Point[] resultPower = {Point.with(0, 0)};
        worldModel.getRelatives().stream().filter(r -> r instanceof Stone.Relative)
                .forEach(r -> {
                    resultPower[0].add(powerFunction.apply(ship, (Stone.Relative)r).rotate(ship.getVehicleOrientation()));
                });
        return resultPower[0];
    }

    private List<Point> calculateGunnerPilotPower(Point gunnerPower, Point pilotPower) {
        return Arrays.asList(
                gunnerPower.getInverse().add(pilotPower),
                Point.with(Math.sqrt(gunnerPower.getX() * gunnerPower.getX() - pilotPower.getX() * pilotPower.getX()),
                        Math.sqrt(gunnerPower.getY() * gunnerPower.getY() - pilotPower.getY() * pilotPower.getY()))
        );
    }

    private Point calculateGunnerPilotAngle(Ship.Relative ship) {
        return Point.with(Math.cos(ship.getWeaponOrientation() - ship.getVehicleOrientation()),
                Math.sin(ship.getWeaponOrientation() - ship.getVehicleOrientation()));
    }

    private int chooseAction(List<Point> state) {
        return policy.getAction(state);
    }

    private void refresh(List<Point> currentState, List<Point> prevState, int prevAction, double reward) {
        policy.refresh(reward, prevAction, prevState, currentState);
    }

    private class QNetSelectiveGunnerPlayer extends GunnerPlayer {
        private LinkedList<Pair<Pair<List<Point>, Integer>, Long>> states = new LinkedList<>();
        private List<GunnerPlayer> players = new ArrayList<>();
        private long turnsRemaining = 0;
        private GunnerPlayer currentPlayer;

        public QNetSelectiveGunnerPlayer(long id, List<GunnerAgent> agents) {
            super(id);
            agents.forEach(agent -> players.add(agent.buildPlayer(id)));
        }

        @Override
        public Action chooseAction() {
            if (turnsRemaining == 0) {
                List<Point> state = calculatePowers(worldModel, weapon.getShip());
                int action = QNetSelectiveGunnerAgent.this.chooseAction(state);
                states.addFirst(Pair.of(Pair.of(state, action), 0L));
                if (states.size() > Constants.MAX_DELAY / chooseTurns) {
                    Pair<Pair<List<Point>, Integer>, Long> removed = states.removeLast();
                    refresh(states.getLast().first().first(),
                            removed.first().first(),
                            removed.first().second(),
                            removed.second());
                }
                currentPlayer = players.get(action);
                turnsRemaining = chooseTurns;
                if (QNetSelectiveGunnerAgent.this.isLearningDisabled()) {
                    chooseStatistic.merge(action, 1L, (oldL, newL) -> oldL + newL);
                }
            }
            turnsRemaining--;
            currentPlayer.setRelativeWorldModel(worldModel);
            return currentPlayer.chooseAction();
        }

        @Override
        public void incScore(long reward, long delay) {
            super.incScore(reward, delay/chooseTurns);
            if (states.size() > delay/chooseTurns) {
                states.get((int) (delay/chooseTurns)).setSecond(reward);
            }
        }
    }
}
