package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.QLearningSortedGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.QLearningSortedPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.ExplorationProbability1;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.LearningStep2;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.DoNothingGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.ShootClosestGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;

import java.util.ArrayList;
import java.util.List;

public class AgentsBuilder {

    public List<GunnerAgent> getDefaultGunnerAgents() {
        List<GunnerAgent> result = new ArrayList<>();
        //result.add(new DoNothingGunnerAgent());
        //result.add(new LeftRollNShootGunnerAgent());
        //result.add(new RandomShootGunnerAgent());
        //result.add(new RightRollNShootGunnerAgent());
        //result.add(new ShootForwardGunnerAgent());
        result.add(new ShootClosestGunnerAgent());
        return result;
    }

    public List<PilotAgent> getDefaultPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        result.add(new DoNothingPilotAgent());
        //result.add(new FlyForwardPilotAgent());
        //result.add(new RandomFlyPilotAgent());
        //result.add(new LeftRollNFlyPilotAgent());
        //result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    public List<GunnerAgent> getGunnerAgents(long gamesCount) {
        List<GunnerAgent> result = new ArrayList<>();
        result.addAll(getQLearningGunners(gamesCount));
        return result;
    }


    public List<PilotAgent> getPilotAgents(long gamesCount) {
        List<PilotAgent> result = new ArrayList<>();
        result.addAll(getQLearningPilots(gamesCount));
        return result;
    }

    private static List<PilotAgent> getQLearningPilots(long gamesCount) {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
        SortedEntitiesDataDescriptor descriptor1 = new SortedEntitiesDataDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        SortedEntitiesDataDescriptor descriptor2 = new SortedEntitiesDataDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.setLimit(2);
        for (int i = 1; i < max; i++) {
            descriptor2.splitAngle(i);
        }
        SortedEntitiesDataDescriptor descriptor3 = new SortedEntitiesDataDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.setLimit(3);
        for (int i = 1; i < max/4; i++) {
            descriptor3.splitAngle(i * 4);
        }
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        descriptor4.splitAngle(6);
        descriptor4.splitAngle(max - 6);
        descriptor4.splitAngle(15);
        descriptor4.splitAngle(max - 15);
        descriptor4.splitAngle(25);
        descriptor4.splitAngle(max - 25);
        descriptor4.splitAngle(34);
        descriptor4.splitAngle(max - 34);
        descriptor4.splitAngle(40);
        descriptor4.splitDistance(Constants.STONE_RADIUS * 3);
        List<PilotAgent> result = new ArrayList<>();
        result.add(new QLearningSortedPilotAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        result.add(new QLearningSortedPilotAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        result.add(new QLearningSortedPilotAgent(descriptor3,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        return result;
    }

    public List<GunnerAgent> getQLearningGunners(long gamesCount) {
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        SortedEntitiesDataDescriptor descriptor1 = new SortedEntitiesDataDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        SortedEntitiesDataDescriptor descriptor2 = new SortedEntitiesDataDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.setLimit(2);
        for (int i = 1; i < max; i++) {
            descriptor2.splitAngle(i);
        }
        SortedEntitiesDataDescriptor descriptor3 = new SortedEntitiesDataDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.setLimit(3);
        for (int i = 1; i < max/4; i++) {
            descriptor3.splitAngle(i * 4);
        }
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.splitAngle(3);
        descriptor4.splitAngle(max - 3);
        descriptor4.splitAngle(10);
        descriptor4.splitAngle(max - 10);
        descriptor4.splitAngle(20);
        descriptor4.splitAngle(max - 20);
        descriptor4.splitAngle(30);
        descriptor4.splitAngle(max - 30);
        descriptor4.splitAngle(40);
        descriptor4.splitDistance(Constants.STONE_RADIUS * 5);
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new QLearningSortedGunnerAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        result.add(new QLearningSortedGunnerAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        result.add(new QLearningSortedGunnerAgent(descriptor3,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        result.add(new QLearningPolarGunnerAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        return result;
    }
}
