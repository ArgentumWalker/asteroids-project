package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.selective_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class QNetSelectiveGunnerAgent extends GunnerAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final BiFunction<Ship.Relative, Stone.Relative, Point> powerFunction;
    private final SelectiveQNet policy;
    private final List<GunnerAgent> determenisticGunnerAgents;
    private final long chooseTurns;

    private final ConcurrentHashMap<Integer, Long> chooseStatistic = new ConcurrentHashMap<>();

    public QNetSelectiveGunnerAgent(
            BiFunction<Ship.Relative, Stone.Relative, Point> powerFunction,
            List<GunnerAgent> determenisticGunnerAgents,
            long chooseTurns,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int featuresPower
    ) {
        this.powerFunction = powerFunction;
        this.determenisticGunnerAgents = determenisticGunnerAgents;
        this.chooseTurns = chooseTurns;
        this.policy = new SelectiveQNet(explorationProbability, alpha, gamma, determenisticGunnerAgents.size(),
                5,
                this::isLearningDisabled,
                featuresPower);
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

    public QNetSelectiveGunnerAgent setRefreshDelay(long delay) {
        policy.setRefreshDelay(delay);
        return this;
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new QNetSelectiveGunnerPlayer(id, determenisticGunnerAgents);
    }

    private List<Point> calculatePowers(RelativeWorldModel worldModel, Ship ship) {
        List<Point> result = new ArrayList<>();
        Ship.Relative shipRelative = worldModel.getRelatives().stream()
                .filter(r -> r instanceof Ship.Relative && r.getEntity().equals(ship))
                .map(r -> (Ship.Relative)r)
                .findAny().get();
        Point gunnerPower = calculateGunnerPower(worldModel, shipRelative);
        result.add(gunnerPower);
        Point pilotPower = calculateGunnerPower(worldModel, shipRelative);
        result.add(pilotPower);
        result.addAll(calculateGunnerPilotPower(gunnerPower, pilotPower));
        result.add(calculateGunnerPilotAngle(shipRelative));
        Point shipVelocity = shipRelative.getEntity().getVelocity().clone();
        result.add(shipVelocity.rotate(ship.getVehicle().getRealAngle()));
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
        return Collections.singletonList(
                gunnerPower.getInverse().add(pilotPower)
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
                if (states.size() > Constants.AGENT_LEARNING_MAX_DELAY / chooseTurns) {
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
