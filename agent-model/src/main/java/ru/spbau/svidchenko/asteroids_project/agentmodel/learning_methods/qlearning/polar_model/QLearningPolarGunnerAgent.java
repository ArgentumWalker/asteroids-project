package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.*;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class QLearningPolarGunnerAgent extends PolarGridGunnerAgent {
    private static long freeId = 0;
    private final long id = freeId++;
    private Callable<Double> explorationProbability;
    private final double gamma;
    private Callable<Double> alpha;
    private ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Double>> state2action2value = new ConcurrentHashMap<>();

    public QLearningPolarGunnerAgent(
            PolarGridDescriptor polarGridDescriptor,
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
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

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        this.explorationProbability = explorationProbability;
    }

    public void setAlpha(Callable<Double> alpha) {
        this.alpha = alpha;
    }

    @Override
    protected PolarGridAgentGunnerPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        return new QLearningPolarGunnerPlayer(id, polarGridDescriptor);
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
        prevStateAction.put(prevAction, prevActionValue + alpha.call() * (reward + gamma * maxValue - prevActionValue));
    }

    private boolean isExploration() {
        return RandomGod.ask.nextDouble() < explorationProbability.call();
    }

    private int randomAction() {
        return RandomGod.ask.nextInt(5);
    }

    private double getMaxValue(ConcurrentHashMap<Integer, Double> action2value) {
        double max = -1e10;
        for (int action = 0; action < 5; action++) {
            max = Math.max(max, getByAction(action2value, action));
        }
        return max;
    }

    private int getActionWithMaxValue(ConcurrentHashMap<Integer, Double> action2value) {
        double max = -1e10;
        int action = 0;
        for (int i = 0; i < 5; i++) {
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

    private class QLearningPolarGunnerPlayer extends PolarGridAgentGunnerPlayer {
        private long prevState = 0;
        private int prevAction = 0;
        private double reward = 0;

        public QLearningPolarGunnerPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
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
            int action = QLearningPolarGunnerAgent.this.chooseAction(newState, prevState, prevAction, reward);
            prevState = newState;
            prevAction = action;
            reward = 0;
            return intToAction(action);
        }


        private Action intToAction(int action) {
            Action result = new Action();
            switch (action % 2) {
                case 1: {
                    result.setShoot(true);
                    break;
                }
                case 2: {
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