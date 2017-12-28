package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGrid;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridAgentGunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QNetPolarGunnerAgent extends PolarGridGunnerAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final PolarQNet qNet;

    public QNetPolarGunnerAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(polarGridDescriptor);
        qNet = new PolarQNet(explorationProbability, alpha, gamma, 6, polarGridDescriptor, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QNetPolarGunnerAgent_" + id;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        qNet.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        qNet.setAlpha(alpha);
    }

    @Override
    protected PolarGridAgentGunnerPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        return new QNetPolarGunnerPlayer(id, polarGridDescriptor);
    }

    private int chooseAction(List<List<Integer>> state) {
        return qNet.getAction(state);
    }

    private void refresh(List<List<Integer>> currentState, List<List<Integer>> prevState, int prevAction, double reward) {
        qNet.refresh(reward, prevAction, prevState, currentState);
    }

    private class QNetPolarGunnerPlayer extends PolarGridAgentGunnerPlayer {
        LinkedList<Pair<Pair<List<List<Integer>>, Integer>, Long>> states = new LinkedList<>();

        public QNetPolarGunnerPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
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
            int action = QNetPolarGunnerAgent.this.chooseAction(copy);
            states.addFirst(Pair.of(Pair.of(copy, action), 0L));
            if (states.size() > Constants.AGENT_LEARNING_MAX_DELAY) {
                Pair<Pair<List<List<Integer>>, Integer>, Long> removed = states.removeLast();
                refresh(states.getLast().first().first(),
                        removed.first().first(),
                        removed.first().second(),
                        removed.second());
            }
            return intToAction(action);
        }

        private Action intToAction(int action) {
            Action result = new Action();
            switch (action % 2) {
                case 1: {
                    result.setShoot(true);
                    break;
                }
                case 0: {
                    result.setShoot(false);
                    break;
                }
            }
            switch (action / 2) {
                case 0: {
                    result.setTurn(Action.Turn.LEFT);
                    break;
                }
                case 1: {
                    result.setTurn(Action.Turn.NO_TURN);
                    break;
                }
                case 2: {
                    result.setTurn(Action.Turn.RIGHT);
                    break;
                }
            }
            return result;
        }
    }
}
