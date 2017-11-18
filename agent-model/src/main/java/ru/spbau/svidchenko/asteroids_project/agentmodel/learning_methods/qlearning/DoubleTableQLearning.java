package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DoubleTableQLearning extends TableQLearningBase {
    private final long turnsToEstimationRefresh;
    private final AtomicLong turnsCount = new AtomicLong(0);
    private ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Double>> state2action2estimatedValue = new ConcurrentHashMap<>();


    public DoubleTableQLearning(
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int actionCount,
            Callable<Boolean> isLearningDisabled,
            long turnsToEstimationRefresh
    ) {
        super(explorationProbability, alpha, gamma, actionCount, isLearningDisabled);
        this.turnsToEstimationRefresh = turnsToEstimationRefresh;
    }

    @Override
    public int chooseAction(long state) {
        return !isLearningDisabled.call() ? super.chooseAction(state) : getActionWithMaxValue(getEstimatedByState(state));
    }

    @Override
    public void refresh(long currentState, long prevState, int prevAction, double reward) {
        if (isLearningDisabled.call()) {
            return;
        }
        ConcurrentHashMap<Integer, Double> currentStateAction = getEstimatedByState(currentState);
        ConcurrentHashMap<Integer, Double> prevStateAction = getByState(prevState);
        double maxValue = getMaxValue(currentStateAction);
        double prevActionValue = prevStateAction.get(prevAction);
        prevStateAction.put(prevAction, prevActionValue + alpha.call() * (reward + gamma * maxValue - prevActionValue));
        refreshEstimation();
    }

    private ConcurrentHashMap<Integer, Double> getEstimatedByState(long state) {
        if (state2action2estimatedValue.containsKey(state)) {
            return state2action2estimatedValue.get(state);
        }
        ConcurrentHashMap<Integer, Double> result = new ConcurrentHashMap<>();
        for (int i = 0; i < actionCount; i++) {
            result.put(i, 0.);
        }
        state2action2estimatedValue.put(state, result);
        return result;
    }

    private void refreshEstimation() {
        if (turnsCount.incrementAndGet() % turnsToEstimationRefresh == 0) {
            for (ConcurrentHashMap.Entry<Long, ConcurrentHashMap<Integer, Double>> entry : state2action2value.entrySet()) {
                getEstimatedByState(entry.getKey()).putAll(entry.getValue());
            }
        }
    }
}
