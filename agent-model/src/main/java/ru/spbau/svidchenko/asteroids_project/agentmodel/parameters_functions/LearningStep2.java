package ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;

import java.util.concurrent.atomic.AtomicLong;

public class LearningStep2 implements Callable<Double> {
    private final double startStep;
    private final double targetStep;
    private final double turnsConst;
    private final AtomicLong turns = new AtomicLong(0);

    public LearningStep2(double turnsConst, double targetStep, double startStep) {
        this.startStep = startStep;
        this.targetStep = targetStep;
        this.turnsConst = turnsConst;
    }

    @Override
    public Double call() {
        return targetStep - (targetStep - startStep) / (1 + turns.getAndIncrement() / turnsConst);
    }
}
