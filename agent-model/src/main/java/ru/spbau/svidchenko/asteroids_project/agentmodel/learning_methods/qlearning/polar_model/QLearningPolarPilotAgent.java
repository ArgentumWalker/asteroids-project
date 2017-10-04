package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGrid;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridAgentPilotPlayer;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridPilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class QLearningPolarPilotAgent extends PolarGridPilotAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private final Callable<Double> explorationProbability;
    private final double gamma;
    private final double alpha;
    private ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Double>> state2action2value = new ConcurrentHashMap<>();

    public QLearningPolarPilotAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            double alpha,
            double gamma
    ) {
        super(polarGridDescriptor);
        this.explorationProbability = explorationProbability;
        this.gamma = gamma;
        this.alpha = alpha;
    }

    @Override
    public String getName() {
        return "QLearningPolarPilotAgent_" + id;
    }

    @Override
    protected PolarGridAgentPilotPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        return new QLearningPolarPilotPlayer(id, polarGridDescriptor);
    }

    private int chooseAction(long state, long prevState, int prevAction, double reward) {
        refresh(state, prevState, prevAction, reward);
        return isExploration() ? randomAction() : getActionWithMaxValue(getByState(state));
    }

    private void refresh(long currentState, long prevState, int prevAction, double reward) {
        ConcurrentHashMap<Integer, Double> currentStateAction = getByState(currentState);
        ConcurrentHashMap<Integer, Double> prevStateAction = getByState(prevState);
        double maxValue = getMaxValue(currentStateAction);
        double prevActionValue = getByAction(prevStateAction, prevAction);
        prevStateAction.put(prevAction, prevActionValue + alpha * (reward + gamma * maxValue - prevActionValue));
    }
    
    private boolean isExploration() {
        return RandomGod.ask.nextDouble() < explorationProbability.call();
    }

    private int randomAction() {
        return RandomGod.ask.nextInt(9);
    }

    private double getMaxValue(ConcurrentHashMap<Integer, Double> action2value) {
        double max = -1e10;
        for (int action = 0; action < 9; action++) {
            max = Math.max(max, getByAction(action2value, action));
        }
        return max;
    }

    private int getActionWithMaxValue(ConcurrentHashMap<Integer, Double> action2value) {
        double max = -1e10;
        int action = 0;
        for (int i = 0; i < 9; i++) {
            double value = getByAction(action2value, i);
            if (max < value) {
                max = value;
                action = i;
            }
        }
        return action;
    }

    private ConcurrentHashMap<Integer, Double> getByState(long state) {
        if (state2action2value.containsKey(state)) {
            return state2action2value.get(state);
        }
        ConcurrentHashMap<Integer, Double> result = new ConcurrentHashMap<>();
        state2action2value.put(state, result);
        return result;
    }

    private double getByAction(ConcurrentHashMap<Integer, Double> action2value, int action) {
        if (action2value.containsKey(action)) {
            return action2value.get(action);
        }
        action2value.put(action, 0.0);
        return 0.0;
    }

    private class QLearningPolarPilotPlayer extends PolarGridAgentPilotPlayer {
        private long prevState = 0;
        private int prevAction = 0;
        private double reward = 0;

        public QLearningPolarPilotPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
            super(id, polarGridDescriptor);
        }

        @Override
        public void incScore(long reward) {
            super.incScore(reward);
            this.reward = reward;
        }

        @Override
        protected Action chooseAction(PolarGrid polarGrid) {
            long newState = 0;
            for (List<Boolean> booleans : polarGrid.getValues()) {
                for (boolean bool : booleans) {
                    newState = newState * 2 + (bool ? 1 : 0);
                }
            }
            int action = QLearningPolarPilotAgent.this.chooseAction(newState, prevState, prevAction, reward);
            prevState = newState;
            prevAction = action;
            reward = 0;
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
