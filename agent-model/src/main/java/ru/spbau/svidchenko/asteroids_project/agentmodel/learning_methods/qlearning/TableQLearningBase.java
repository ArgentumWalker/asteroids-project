package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class TableQLearningBase implements Serializable {
    protected final int actionCount;
    protected Callable<Double> explorationProbability;
    protected final double gamma;
    protected Callable<Double> alpha;
    protected ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Double>> state2action2value = new ConcurrentHashMap<>();
    protected final Callable<Boolean> isLearningDisabled;

    public TableQLearningBase(
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int actionCount,
            Callable<Boolean> isLearningDisabled
    ) {
        this.isLearningDisabled = isLearningDisabled;
        this.explorationProbability = explorationProbability;
        this.gamma = gamma;
        this.alpha = alpha;
        this.actionCount = actionCount;
    }

    public void setExplorationProbability(Callable<Double> explorationProbability) {
        this.explorationProbability = explorationProbability;
    }

    public void setAlpha(Callable<Double> alpha) {
        this.alpha = alpha;
    }

    public int chooseAction(long state) {
        return !isLearningDisabled.call() && isExploration() ? randomAction() : getActionWithMaxValue(getByState(state));
    }

    public void refresh(long currentState, long prevState, int prevAction, double reward) {
        if (isLearningDisabled.call()) {
            return;
        }
        ConcurrentHashMap<Integer, Double> currentStateAction = getByState(currentState);
        ConcurrentHashMap<Integer, Double> prevStateAction = getByState(prevState);
        double maxValue = getMaxValue(currentStateAction);
        double prevActionValue = prevStateAction.get(prevAction);
        prevStateAction.put(prevAction, prevActionValue + alpha.call() * (reward + gamma * maxValue - prevActionValue));
    }

    protected boolean isExploration() {
        return RandomGod.ask.nextDouble() < explorationProbability.call();
    }

    protected int randomAction() {
        return RandomGod.ask.nextInt(actionCount);
    }

    protected double getMaxValue(ConcurrentHashMap<Integer, Double> action2value) {
        double max = -1e10;
        for (int action = 0; action < actionCount; action++) {
            max = Math.max(max, action2value.get(action));
        }
        return max;
    }

    protected int getActionWithMaxValue(ConcurrentHashMap<Integer, Double> action2value) {
        double max = -1e10;
        int action = 0;
        for (int i = 0; i < actionCount; i++) {
            double value = action2value.get(i);
            if (max < value) {
                max = value;
                action = i;
            }
        }
        return action;
    }

    protected ConcurrentHashMap<Integer, Double> getByState(long state) {
        if (state2action2value.containsKey(state)) {
            return state2action2value.get(state);
        }
        ConcurrentHashMap<Integer, Double> result = new ConcurrentHashMap<>();
        for (int i = 0; i < actionCount; i++) {
            result.put(i, 0.);
        }
        state2action2value.put(state, result);
        return result;
    }
}
