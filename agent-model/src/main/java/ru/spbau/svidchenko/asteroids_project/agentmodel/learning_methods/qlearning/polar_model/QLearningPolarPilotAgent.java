package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.TableQLearningBase;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGrid;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridAgentPilotPlayer;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridPilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QLearningPolarPilotAgent extends PolarGridPilotAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final TableQLearningBase qLearningBase;
    private final Map<Integer, Double> bonusRewards = new HashMap<>();

    public QLearningPolarPilotAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma
    ) {
        super(polarGridDescriptor);
        bonusRewards.put(0, 0.);
        bonusRewards.put(1, 0.);
        bonusRewards.put(2, 0.);
        qLearningBase = new TableQLearningBase(explorationProbability, alpha, gamma, 9, this::isLearningDisabled);
    }

    @Override
    public String getName() {
        return "QLearningPolarPilotAgent_" + id;
    }

    public void setBonusReward(int action, double bonus) {
        bonusRewards.put(action, bonus);
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        qLearningBase.setExplorationProbability(explorationProbability);
    }

    public void setAlpha(Callable<Double> alpha) {
        qLearningBase.setAlpha(alpha);
    }

    @Override
    protected PolarGridAgentPilotPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        return new QLearningPolarPilotPlayer(id, polarGridDescriptor);
    }

    private int chooseAction(long state) {
        return qLearningBase.chooseAction(state);
    }

    private void refresh(long currentState, long prevState, int prevAction, double reward) {
        qLearningBase.refresh(currentState, prevState, prevAction, reward);
    }

    private class QLearningPolarPilotPlayer extends PolarGridAgentPilotPlayer {
        LinkedList<Pair<Pair<Pair<Long, Long>, Integer>, Long>> states = new LinkedList<>();

        public QLearningPolarPilotPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
            super(id, polarGridDescriptor);
        }

        @Override
        public void incScore(long reward, long delay) {
            super.incScore(reward, delay);
            if (states.size() > delay) {
                states.get((int) delay).setSecond(states.get((int) delay).second() + reward);
            }
        }

        @Override
        protected Action chooseAction(PolarGrid polarGrid) {
            long newState = 0;
            long symmState = 0;
            for (List<Integer> counts : polarGrid.getValues()) {
                for (int count : counts) {
                    newState = newState * 2 + (count > 0 ? 1 : 0);
                }
                Collections.reverse(counts);
                for (int count : counts) {
                    newState = newState * 2 + (count > 0 ? 1 : 0);
                }
            }
            int action = QLearningPolarPilotAgent.this.chooseAction(newState);
            states.addFirst(Pair.of(Pair.of(Pair.of(newState, symmState), action), 0L));
            if (states.size() > Constants.MAX_DELAY) {
                Pair<Pair<Pair<Long, Long>, Integer>, Long> removed = states.removeLast();
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
