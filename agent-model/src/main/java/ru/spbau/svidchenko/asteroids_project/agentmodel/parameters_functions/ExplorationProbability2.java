package ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;

import java.util.concurrent.atomic.AtomicLong;

public class ExplorationProbability2 implements ExplorationProbability {
    private final double probabilityConstant;

    public ExplorationProbability2(double targetProbability) {
        probabilityConstant = targetProbability;
    }

    @Override
    public Double call() {
        return probabilityConstant;
    }

    @Override
    public void reset() {
        //Do nothing
    }
}
