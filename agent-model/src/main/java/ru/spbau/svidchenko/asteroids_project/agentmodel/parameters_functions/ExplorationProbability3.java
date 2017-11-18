package ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;

import java.util.concurrent.atomic.AtomicLong;

public class ExplorationProbability3 implements Callable<Double> {
    private final double probabilityConstant;
    private final long switchTurns;
    private double currentProbability = 1.0;
    private final AtomicLong step = new AtomicLong(1);
    private final AtomicLong turns = new AtomicLong(0);

    public ExplorationProbability3(long switchTurns, double switchProbability) {
        this.switchTurns = switchTurns;
        probabilityConstant = switchProbability;
    }

    @Override
    public Double call() {
        step.getAndUpdate(l -> {
            if (l * switchTurns < turns.getAndIncrement()) {
                currentProbability *= probabilityConstant;
                return l + 1;
            } else {
                return l;
            }
        });
        return currentProbability;
    }
}
