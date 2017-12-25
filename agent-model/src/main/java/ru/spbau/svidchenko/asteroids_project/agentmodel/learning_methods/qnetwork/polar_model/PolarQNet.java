package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.QNet;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PolarQNet extends QNet {

    public PolarQNet(
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int actionCount,
            PolarGridDescriptor descriptor,
            Callable<Boolean> isLearningDisabled
    ) {
        super(explorationProbability, alpha, gamma, actionCount,
                descriptor.getAngleSectors().size() * descriptor.getDistanceSectors().size(), isLearningDisabled);
    }

    public void refresh(double reward, int action, List<List<Integer>> oldState, List<List<Integer>> newState) {
        refreshNet(reward, action, getCommonState(oldState), getCommonState(newState));
    }

    public int getAction(List<List<Integer>> state) {
        return getNetAction(getCommonState(state));
    }

    public List<Double> getCommonState(List<List<Integer>> state) {
        List<Double> realState = new ArrayList<>();
        for (List<Integer> subState : state) {
            for (int subSubState : subState) {
                realState.add((double)subSubState);
            }
        }
        return realState;
    }

    public PolarQNet setRefreshDelay(long delay) {
        super.setRefreshDelay(delay);
        return this;
    }
}
