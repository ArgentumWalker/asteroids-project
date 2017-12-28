package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.TableQLearningBase;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.ExplorationProbability;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesData;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;

import java.util.LinkedList;

public class QLearningSortedGunnerAgent extends SortedEntitiesGunnerAgent {
    private static long freeId = 0;
    protected final long id = freeId++;
    private boolean disableSymmetry = false;
    protected TableQLearningBase qLearningBase;
    protected final SortedEntitiesDataDescriptor descriptor;
    protected final ExplorationProbability explorationProbability;

    public QLearningSortedGunnerAgent(
            SortedEntitiesDataDescriptor descriptor,
            ExplorationProbability explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(descriptor);
        this.descriptor = descriptor;
        disableSymmetry = descriptor.isDisableSymmetry();
        this.explorationProbability = explorationProbability;
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

    @Override
    public void resetLearningProbability() {
        super.resetLearningProbability();
        explorationProbability.reset();
    }

    private int chooseAction(long state) {
        return qLearningBase.chooseAction(state);
    }

    private void refresh(long currentState, long prevState, int prevAction, double reward) {
        qLearningBase.refresh(currentState, prevState, prevAction, reward);
    }

    public void setDisableSymmetry(boolean disableSymmetry) {
        this.disableSymmetry = disableSymmetry;
    }

    private class Gunner extends SortedEntitiesGunnerAgent.Gunner {
        LinkedList<Pair<Pair<Pair<Long, Long>, Integer>, Long>> states = new LinkedList<>();

        public Gunner(long id, SortedEntitiesDataDescriptor descriptor) {
            super(id, descriptor);
        }

        @Override
        public void incScore(long reward, long delay) {
            super.incScore(reward, delay);
            if (states.size() > delay) {
                states.get((int) delay).setSecond(states.get((int) delay).second() + reward);
            }
        }

        @Override
        protected Action chooseAction(SortedEntitiesData data) {
            long newState = data.getCurrentState();
            int action = QLearningSortedGunnerAgent.this.chooseAction(newState);
            states.addFirst(Pair.of(Pair.of(Pair.of(newState, data.getCurrentSymmetricState()), action), 0L));
            if (states.size() > Constants.AGENT_LEARNING_MAX_DELAY) {
                Pair<Pair<Pair<Long, Long>, Integer>, Long> removed = states.removeLast();
                double reward = removed.second();
                long angle = descriptor.getAngleSectors()
                        .get((int)(removed.first().first().first() % descriptor.getAngleSectors().size()));
                angle -= 40;
                if (angle == 0) {
                    angle = 1;
                }
                if (angle < 0) {
                    angle = - angle;
                }
                reward += descriptor.getCloserReward() / angle;
                refresh(states.getLast().first().first().first(),
                        removed.first().first().first(),
                        removed.first().second(),
                        reward);
                if (!disableSymmetry) {
                    reward = removed.second();
                    angle = descriptor.getAngleSectors()
                            .get((int) (removed.first().first().second() % descriptor.getAngleSectors().size()));
                    angle -= 40;
                    if (angle == 0) {
                        angle = 1;
                    }
                    if (angle < 0) {
                        angle = -angle;
                    }
                    reward += descriptor.getCloserReward() / angle;
                    refresh(states.getLast().first().first().second(),
                            removed.first().first().second(),
                            2 * (2 - removed.first().second() / 2) + removed.first().second() % 2,
                            reward);
                }
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
