package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.*;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QNetPolarPilotAgent extends PolarGridPilotAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final PolarQNet qNet;

    public QNetPolarPilotAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(polarGridDescriptor);
        qNet = new PolarQNet(explorationProbability, alpha, gamma, 9, polarGridDescriptor, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QNetPolarPilotAgent_" + id;
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
        LinkedList<Pair<Pair<List<List<Integer>>, Integer>, Long>> states = new LinkedList<>();

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
            for (List<Integer> counts : polarGrid.getValues()) {
                List<Integer> c  = new ArrayList<>();
                for (int count : counts) {
                    c.add(count);
                }
                copy.add(c);
            }
            int action = QNetPolarPilotAgent.this.chooseAction(copy);
            states.addFirst(Pair.of(Pair.of(copy, action), 0L));
            if (states.size() > Constants.MAX_DELAY) {
                Pair<Pair<List<List<Integer>>, Integer>, Long> removed = states.removeLast();
                refresh(states.getLast().first().first(),
                        removed.first().first(),
                        removed.first().second(),
                        removed.second());
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