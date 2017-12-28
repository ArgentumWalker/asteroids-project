package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.*;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.util.*;

public class QNetPolarPilotAgent extends PolarGridPilotAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final PolarQNet qNet;
    private final Map<Integer, Double> bonusRewards = new HashMap<>();

    public QNetPolarPilotAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(polarGridDescriptor);
        bonusRewards.put(0, 0.);
        bonusRewards.put(1, 0.);
        bonusRewards.put(2, 0.);
        qNet = new PolarQNet(explorationProbability, alpha, gamma, 9, polarGridDescriptor, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QNetPolarPilotAgent_" + id;
    }

    public QNetPolarPilotAgent setBonusReward(int action, double bonus) {
        bonusRewards.put(action, bonus);
        return this;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        qNet.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        qNet.setAlpha(alpha);
    }

    public QNetPolarPilotAgent setRefreshDelay(long delay) {
        qNet.setRefreshDelay(delay);
        return this;
    }

    @Override
    protected PolarGridAgentPilotPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        return new QNetPolarPilotPlayer(id, polarGridDescriptor);
    }

    private int chooseAction(List<List<Integer>> state) {
        return qNet.getAction(state);
    }

    private void refresh(List<List<Integer>> currentState, List<List<Integer>> prevState, int prevAction, double reward) {
        qNet.refresh(reward, prevAction, prevState, currentState);
    }

    private class QNetPolarPilotPlayer extends PolarGridAgentPilotPlayer {
        LinkedList<Pair<Pair<Pair<List<List<Integer>>, List<List<Integer>>>, Integer>, Long>> states = new LinkedList<>();

        public QNetPolarPilotPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
            super(id, polarGridDescriptor);
        }

        @Override
        public void incScore(long reward, long delay) {
            super.incScore(reward, delay);
            if (states.size() > delay) {
                states.get((int) delay).setSecond(reward);
            }
        }

        @Override
        protected Action chooseAction(PolarGrid polarGrid) {
            List<List<Integer>> copy = new ArrayList<>();
            List<List<Integer>> rewCopy = new ArrayList<>();
            for (List<Integer> counts : polarGrid.getValues()) {
                List<Integer> c  = new ArrayList<>();
                List<Integer> rc = new ArrayList<>();
                for (int count : counts) {
                    c.add(count);
                    rc.add(count);
                }
                Collections.reverse(rc);
                copy.add(c);
                rewCopy.add(rc);
            }
            int action = QNetPolarPilotAgent.this.chooseAction(copy);
            states.addFirst(Pair.of(Pair.of(Pair.of(copy, rewCopy), action), 0L));
            if (states.size() > Constants.AGENT_LEARNING_MAX_DELAY) {
                Pair<Pair<Pair<List<List<Integer>>, List<List<Integer>>>, Integer>, Long> removed = states.removeLast();
                double reward = removed.second() + bonusRewards.get(removed.first().second() % 3);
                refresh(states.getLast().first().first().first(),
                        removed.first().first().first(),
                        removed.first().second(),
                        reward);
                refresh(states.getLast().first().first().second(),
                        removed.first().first().second(),
                        3 * (2 - removed.first().second() / 3) + removed.first().second() % 3,
                        reward);
            }
            return intToAction(action);
        }

        private PilotPlayer.Action intToAction(int action) {
            PilotPlayer.Action result = new PilotPlayer.Action();
            switch (action % 3) {
                case 0: {
                    result.setMove(PilotPlayer.Action.Move.FORWARD);
                    break;
                }
                case 1: {
                    result.setMove(PilotPlayer.Action.Move.BACKWARD);
                    break;
                }
                case 2: {
                    result.setMove(PilotPlayer.Action.Move.OFF);
                    break;
                }
            }
            switch (action / 3) {
                case 0: {
                    result.setTurn(PilotPlayer.Action.Turn.LEFT);
                    break;
                }
                case 1: {
                    result.setTurn(PilotPlayer.Action.Turn.NO_TURN);
                    break;
                }
                case 2: {
                    result.setTurn(PilotPlayer.Action.Turn.RIGHT);
                    break;
                }
            }
            return result;
        }
    }
}
