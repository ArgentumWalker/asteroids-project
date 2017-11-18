package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.TableQLearningBase;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGrid;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridAgentGunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesData;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class QLearningSortedGunnerAgent extends SortedEntitiesGunnerAgent {
    private static long freeId = 0;
    protected final long id = freeId++;
    protected TableQLearningBase qLearningBase;

    public QLearningSortedGunnerAgent(
            SortedEntitiesDataDescriptor descriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(descriptor);
        qLearningBase = new TableQLearningBase(explorationProbability, alpha, gamma, 6, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QLearningSortedGunnerAgent_" + id;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        qLearningBase.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        qLearningBase.setAlpha(alpha);
    }

    @Override
    protected Gunner buildPlayer(long id, SortedEntitiesDataDescriptor descriptor) {
        return new QLearningSortedGunnerAgent.Gunner(id, descriptor);
    }

    private int chooseAction(long state) {
        return qLearningBase.chooseAction(state);
    }

    private void refresh(long currentState, long prevState, int prevAction, double reward) {
        qLearningBase.refresh(currentState, prevState, prevAction, reward);
    }

    private class Gunner extends SortedEntitiesGunnerAgent.Gunner {
        LinkedList<Pair<Pair<Long, Integer>, Long>> states = new LinkedList<>();

        public Gunner(long id, SortedEntitiesDataDescriptor descriptor) {
            super(id, descriptor);
        }

        @Override
        public void incScore(long reward, long delay) {
            super.incScore(reward, delay);
            if (states.size() > delay) {
                states.get((int) delay).setSecond(reward);
            }
        }

        @Override
        protected Action chooseAction(SortedEntitiesData data) {
            long newState = data.getCurrentState();
            int action = QLearningSortedGunnerAgent.this.chooseAction(newState);
            states.addFirst(Pair.of(Pair.of(newState, action), 0L));
            if (states.size() > Constants.MAX_DELAY) {
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
