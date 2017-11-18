package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PolarQNet implements Serializable {
    public List<List<Double>> constants = new ArrayList<>();
    public List<List<Double>> newConstants = new ArrayList<>();
    public double gamma;
    public long refreshDelay = 100;
    public final AtomicLong counter = new AtomicLong(0);
    public final ReadWriteLock newConstantsLock = new ReentrantReadWriteLock();
    public final ReadWriteLock constantsLock = new ReentrantReadWriteLock();
    public Callable<Double> alpha;
    public Callable<Double> explorationProbability;
    public Callable<Boolean> isLearningDisabled;

    public PolarQNet(
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int actionCount,
            PolarGridDescriptor descriptor,
            Callable<Boolean> isLearningDisabled
    ) {
        for (int i = 0; i < actionCount; i++) {
            ArrayList<Double> c = new ArrayList<>();
            ArrayList<Double> c2 = new ArrayList<>();
            for (int p = 0; p <= descriptor.getAngleSectors().size(); p++) {
                for (int q = 0; q < descriptor.getDistanceSectors().size(); q++) {
                    c.add(0.0);
                    c2.add(0.0);
                }
            }
            constants.add(c);
            newConstants.add(c2);
        }
        this.gamma = gamma;
        this.alpha = alpha;
        this.explorationProbability = explorationProbability;
        this.isLearningDisabled = isLearningDisabled;
    }

    public void refresh(double reward, int action, List<List<Integer>> oldState, List<List<Integer>> newState) {
        if (isLearningDisabled.call()) {
            return;
        }
        double a = alpha.call();
        //(reward + gamma * Q_new - Q_old)^2
        //Q_old^2 - 2(reward + gamma * Q_new)Q_old + (reward + gamma * Q_new)^2
        double oldValue = 0.0;
        double newValue = -1e6;
        constantsLock.readLock().lock();
        for (int i = 0; i < oldState.size(); i++) {
            for (int j = 0; j < oldState.get(i).size(); j++) {
                oldValue += oldState.get(i).get(j) * constants.get(action).get(j * oldState.size() + i);
            }
        }
        for (int act = 0; act < constants.size(); act++) {
            double tmp = 0.0;
            for (int i = 0; i < oldState.size(); i++) {
                for (int j = 0; j < oldState.get(i).size(); j++) {
                    tmp += newState.get(i).get(j) * constants.get(action).get(j * oldState.size() + i);
                }
            }
            if (tmp > newValue) {
                newValue = tmp;
            }
        }
        constantsLock.readLock().unlock();
        double maxDelta = 0.0;
        for (int i = 0; i < oldState.size(); i++) {
            for (int j = 0; j < oldState.get(i).size(); j++) {
                double delta = Math.abs(oldState.get(i).get(j) * 2 * oldValue
                        - 2 * oldState.get(i).get(j) * (reward + gamma * newValue)
                        - 2 * gamma * newState.get(i).get(j) * oldValue
                        + 2 * (reward + gamma * newValue) * newState.get(i).get(j));
                if (maxDelta < delta) {
                    maxDelta = delta;
                }
            }
        }
        if (maxDelta > 1.0) {
            a = a/maxDelta;
        }
        newConstantsLock.writeLock().lock();
        for (int i = 0; i < oldState.size(); i++) {
            for (int j = 0; j < oldState.get(i).size(); j++) {
                double delta = oldState.get(i).get(j) * 2 * oldValue
                        - 2 * oldState.get(i).get(j) * (reward + gamma * newValue)
                        - 2 * gamma * newState.get(i).get(j) * oldValue
                        + 2 * (reward + gamma * newValue) * newState.get(i).get(j);
                newConstants.get(action).set(j * oldState.size() + i,
                        newConstants.get(action).get(j * oldState.size() + i) - a * delta);
            }
        }
        newConstantsLock.writeLock().unlock();
        if (counter.getAndIncrement() == refreshDelay) {
            constantsLock.writeLock().lock();
            newConstantsLock.readLock().lock();
            counter.getAndUpdate((x) -> x - refreshDelay);
            for (int i = 0; i < constants.size(); i++) {
                for (int j = 0; j < constants.get(i).size(); j++) {
                    constants.get(i).set(j, newConstants.get(i).get(j));
                }
            }
            newConstantsLock.readLock().unlock();
            constantsLock.writeLock().unlock();
        }
    }

    public int getAction(List<List<Integer>> state) {
        if (!isLearningDisabled.call() && RandomGod.ask.nextDouble() < explorationProbability.call()) {
            return RandomGod.ask.nextInt(constants.size());
        }
        double maxValue = -1e6;
        int bestAction = 0;
        newConstantsLock.readLock().lock();
        for (int action = 0; action < newConstants.size(); action++) {
            double newValue = 0.0;
            for (int i = 0; i < state.size(); i++) {
                for (int j = 0; j < state.get(i).size(); j++) {
                    newValue += state.get(i).get(j) * newConstants.get(action).get(j * state.size() + i);
                }
            }
            if (newValue > maxValue) {
                maxValue = newValue;
                bestAction = action;
            }
        }
        newConstantsLock.readLock().unlock();
        return bestAction;
    }


    public void setExplorationProbability(Callable<Double> explorationProbability) {
        this.explorationProbability = explorationProbability;
    }

    public void setAlpha(Callable<Double> alpha) {
        this.alpha = alpha;
    }

    public PolarQNet setRefreshDelay(long delay) {
        refreshDelay = delay;
        return this;
    }
}
