package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.TableQLearningBase;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.*;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;

import java.util.LinkedList;
import java.util.List;

public class QLearningPolarGunnerAgent extends PolarGridGunnerAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final TableQLearningBase qLearningBase;

    public QLearningPolarGunnerAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(polarGridDescriptor);
        qLearningBase = new TableQLearningBase(explorationProbability, alpha, gamma, 6, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QLearningPolarGunnerAgent_" + id;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        qLearningBase.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        qLearningBase.setAlpha(alpha);
    }

    @Override
    protected PolarGridAgentGunnerPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        return new QLearningPolarGunnerPlayer(id, polarGridDescriptor);
    }

    private int chooseAction(long state) {
        return qLearningBase.chooseAction(state);
    }

    private void refresh(long currentState, long prevState, int prevAction, double reward) {
        qLearningBase.refresh(currentState, prevState, prevAction, reward);
    }

    private class QLearningPolarGunnerPlayer extends PolarGridAgentGunnerPlayer {
        LinkedList<Pair<Pair<Long, Integer>, Long>> states = new LinkedList<>();

        public QLearningPolarGunnerPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
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
            long newState = 0;
            for (List<Integer> counts : polarGrid.getValues()) {
                for (int count : counts) {
                    newState = newState * 2 + (count > 0 ? 1 : 0);
                }
            }
            int action = QLearningPolarGunnerAgent.this.chooseAction(newState);
            states.addFirst(Pair.of(Pair.of(newState, action), 0L));
            if (states.size() > Constants.AGENT_LEARNING_MAX_DELAY) {
                Pair<Pair<Long, Integer>, Long> removed = states.removeLast();
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
