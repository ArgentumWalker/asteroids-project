package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model.PolarQNet;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class QNet  implements Serializable {
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

    public QNet(
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int actionCount,
            int featuresCount,
            Callable<Boolean> isLearningDisabled
    ) {
        for (int i = 0; i < actionCount; i++) {
            ArrayList<Double> c = new ArrayList<>();
            ArrayList<Double> c2 = new ArrayList<>();
            for (int p = 0; p <= featuresCount; p++) {
                c.add(0.0);
                c2.add(0.0);
            }
            constants.add(c);
            newConstants.add(c2);
        }
        this.gamma = gamma;
        this.alpha = alpha;
        this.explorationProbability = explorationProbability;
        this.isLearningDisabled = isLearningDisabled;
    }

    public final void refreshNet(double reward, int action, List<Double> oldState, List<Double> newState) {
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
            oldValue += oldState.get(i) * constants.get(action).get(i);
        }
        for (int act = 0; act < constants.size(); act++) {
            double tmp = 0.0;
            for (int i = 0; i < oldState.size(); i++) {
                tmp += newState.get(i) * constants.get(action).get(i);
            }
            if (tmp > newValue) {
                newValue = tmp;
            }
        }
        constantsLock.readLock().unlock();
        double maxDelta = -1e10;
        for (int i = 0; i < oldState.size(); i++) {
            double delta = Math.abs(oldState.get(i) * 2 * oldValue
                    - 2 * oldState.get(i) * (reward + gamma * newValue)
                    - 2 * gamma * newState.get(i) * oldValue
                    + 2 * (reward + gamma * newValue) * newState.get(i));
            if (maxDelta < delta) {
                maxDelta = delta;
            }
        }
        if (maxDelta > 1.0) {
            a = a/maxDelta;
        }
        newConstantsLock.writeLock().lock();
        for (int i = 0; i < oldState.size(); i++) {
            double delta = oldState.get(i) * 2 * oldValue
                    - 2 * oldState.get(i) * (reward + gamma * newValue)
                    - 2 * gamma * newState.get(i) * oldValue
                    + 2 * (reward + gamma * newValue) * newState.get(i);
            newConstants.get(action).set(i,
                    newConstants.get(action).get(i) - a * delta);
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

    public final int getNetAction(List<Double> state) {
        if (!isLearningDisabled.call() && RandomGod.ask.nextDouble() < explorationProbability.call()) {
            return RandomGod.ask.nextInt(constants.size());
        }
        double maxValue = -1e6;
        int bestAction = 0;
        newConstantsLock.readLock().lock();
        for (int action = 0; action < newConstants.size(); action++) {
            double newValue = 0.0;
            for (int i = 0; i < state.size(); i++) {
                newValue += state.get(i) * newConstants.get(action).get(i);
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

    public QNet setRefreshDelay(long delay) {
        refreshDelay = delay;
        return this;
    }
}
