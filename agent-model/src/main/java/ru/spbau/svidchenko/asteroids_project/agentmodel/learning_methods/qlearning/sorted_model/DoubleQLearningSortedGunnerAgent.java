package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.DoubleTableQLearning;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.ExplorationProbability;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;

public class DoubleQLearningSortedGunnerAgent extends QLearningSortedGunnerAgent {
    public DoubleQLearningSortedGunnerAgent(
            SortedEntitiesDataDescriptor descriptor,
            ExplorationProbability explorationProbability,
            Callable<Double> alpha,
            double gamma,
            long estimationRefreshConst
    ) {
        super(descriptor, explorationProbability, alpha, gamma);
        qLearningBase = new DoubleTableQLearning(explorationProbability, alpha, gamma,
                6, this::isLearningDisabled, estimationRefreshConst);
    }

    @Override
    public String getName() {
        return "DoubleQLearningSortedGunnerAgent_" + id;
    }
}
