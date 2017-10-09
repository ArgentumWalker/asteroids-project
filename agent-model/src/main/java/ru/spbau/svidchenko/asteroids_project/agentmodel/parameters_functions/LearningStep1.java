package ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;

public class LearningStep1 implements Callable<Double> {
    private final Double c;

    public LearningStep1(Double c) {
        this.c = c;
    }


    @Override
    public Double call() {
        return c;
    }
}
