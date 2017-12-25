package ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;

public interface ExplorationProbability extends Callable<Double> {
    void reset();
}
