package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.TableQLearningBase;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesData;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesPilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class QLearningSortedPilotAgent extends SortedEntitiesPilotAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final TableQLearningBase qLearningBase;

    public QLearningSortedPilotAgent(
            SortedEntitiesDataDescriptor descriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(descriptor);
        qLearningBase = new TableQLearningBase(explorationProbability, alpha, gamma, 9, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QLearningSortedPilotAgent_" + id;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        qLearningBase.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        qLearningBase.setAlpha(alpha);
    }

    @Override
    protected QLearningSortedPilotAgent.Pilot buildPlayer(long id, SortedEntitiesDataDescriptor descriptor) {
        return new QLearningSortedPilotAgent.Pilot(id, descriptor);
    }

    private int chooseAction(long state) {
        return qLearningBase.chooseAction(state);
    }

    private void refresh(long currentState, long prevState, int prevAction, double reward) {
        qLearningBase.refresh(currentState, prevState, prevAction, reward);
    }

    private class Pilot extends SortedEntitiesPilotAgent.Pilot {
        LinkedList<Pair<Pair<Long, Integer>, Long>> states = new LinkedList<>();

        public Pilot(long id, SortedEntitiesDataDescriptor descriptor) {
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
            int action = QLearningSortedPilotAgent.this.chooseAction(newState);
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
