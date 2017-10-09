package ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;

import java.util.concurrent.atomic.AtomicLong;

public class ExplorationProbability1 implements Callable<Double> {
    private final double probabilityConstant;
    private final AtomicLong turns = new AtomicLong(0);

    public ExplorationProbability1(long targetTurns, double targetProbability) {
        probabilityConstant = (1 / targetProbability - 1)/targetTurns;
    }

    @Override
    public Double call() {
        return 1 / (1.0 + turns.getAndIncrement() * probabilityConstant);
    }
}
