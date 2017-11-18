package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.DoubleQLearningSortedGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.QLearningSortedGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.QLearningSortedPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model.QNetPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model.QNetPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.DoNothingGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.ShootClosestGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.ShootClosestToDirectionGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesPilotAgent;
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
        result.add(new ShootClosestToDirectionGunnerAgent(1));
        result.add(new ShootClosestToDirectionGunnerAgent(0.1));
        result.add(new ShootClosestToDirectionGunnerAgent(0.01));
        result.add(new ShootClosestToDirectionGunnerAgent(0.001));
        result.add(new ShootClosestToDirectionGunnerAgent(0.5));
        result.add(new ShootClosestToDirectionGunnerAgent(0.05));
        result.add(new ShootClosestToDirectionGunnerAgent(0.005));
        result.add(new ShootClosestToDirectionGunnerAgent(0.75));
        result.add(new ShootClosestToDirectionGunnerAgent(0.075));
        result.add(new ShootClosestToDirectionGunnerAgent(0.0075));
        result.add(new ShootClosestToDirectionGunnerAgent(0.25));
        result.add(new ShootClosestToDirectionGunnerAgent(0.025));
        result.add(new ShootClosestToDirectionGunnerAgent(0.0025));
        return result;
    }

    public List<PilotAgent> getDefaultPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        //result.add(new DoNothingPilotAgent());
        result.add(new FlyForwardPilotAgent());
        //result.add(new RandomFlyPilotAgent());
        //result.add(new LeftRollNFlyPilotAgent());
        //result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    public List<PilotAgent> getAlternateDefaultPilotAgents() {
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
        PolarGridDescriptor descriptor1 = new PolarGridDescriptor();
        descriptor1.setMaxAngle(max);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        for (int i = 2; i < 40; i++) {
            descriptor1.splitDistance(i * Constants.STONE_RADIUS / 3);
        }
        PolarGridDescriptor descriptor2 = new PolarGridDescriptor();
        descriptor2.setMaxAngle(max);
        for (int i = 1; i < max; i++) {
            descriptor2.splitAngle(i);
        }
        for (int i = 2; i < 40; i++) {
            descriptor2.splitDistance(i * Constants.STONE_RADIUS / 4);
        }
        PolarGridDescriptor descriptor3 = new PolarGridDescriptor();
        descriptor3.setMaxAngle(max);
        for (int i = 1; i < max; i++) {
            descriptor3.splitAngle(i);
        }
        for (int i = 2; i < 40; i++) {
            descriptor3.splitDistance(i * Constants.STONE_RADIUS / 5);
        }
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        for (int i = 1; i < max; i++) {
            descriptor4.splitAngle(i);
        }
        for (int i = 2; i < 40; i++) {
            descriptor4.splitDistance(i * Constants.STONE_RADIUS / 6);
        }
        /*SortedEntitiesDataDescriptor descriptor3 = new SortedEntitiesDataDescriptor();
        descriptor3.setLimit(2);
        descriptor3.setMaxAngle(max);
        for (int i = 1; i < max; i++) {
            descriptor3.splitAngle(i);
        }
        for (int i = 2; i < 40; i++) {
            descriptor3.splitDistance(i * Constants.STONE_RADIUS / 2);
        }*/
/*
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
        descriptor4.splitDistance(Constants.STONE_RADIUS * 4);*/
        List<PilotAgent> result = new ArrayList<>();
        result.add(new QNetPolarPilotAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.075),
                new LearningStep1(0.01),
                0.94));
        result.add(new QNetPolarPilotAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.075),
                new LearningStep1(0.01),
                0.94));
        result.add(new QNetPolarPilotAgent(descriptor3,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.075),
                new LearningStep1(0.01), 0.94));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.075),
                new LearningStep1(0.01), 0.94));
        /*result.add(new QLearningPolarPilotAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep1(0.01),
                0.94));*/
        return result;
    }

    public List<GunnerAgent> getQLearningGunners(long gamesCount) {
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        SortedEntitiesDataDescriptor descriptor1 = new SortedEntitiesDataDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.setVehicleRealted(true);
        descriptor1.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor1.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor2 = new SortedEntitiesDataDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.setVehicleRealted(true);
        descriptor2.setReloadRelated(true);
        descriptor2.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor2.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor2.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new QLearningSortedGunnerAgent(descriptor1,
                new ExplorationProbability3(100 * Constants.TURNS_IN_GAME, 0.95),
                new LearningStep1(0.005),
                0.99));
        result.add(new DoubleQLearningSortedGunnerAgent(descriptor1,
                new ExplorationProbability3(100 * Constants.TURNS_IN_GAME, 0.95),
                new LearningStep1(0.005),
                0.99,
                10 * Constants.TURNS_IN_GAME));
        result.add(new DoubleQLearningSortedGunnerAgent(descriptor1,
                new ExplorationProbability3(100 * Constants.TURNS_IN_GAME, 0.95),
                new LearningStep1(0.005),
                0.99,
                20 * Constants.TURNS_IN_GAME));
        result.add(new DoubleQLearningSortedGunnerAgent(descriptor1,
                new ExplorationProbability3(100 * Constants.TURNS_IN_GAME, 0.95),
                new LearningStep1(0.005),
                0.99,
                30 * Constants.TURNS_IN_GAME));
        /*result.add(new QLearningSortedGunnerAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep1(0.01),
                0.94));*/
        //result.add(new ShootClosestGunnerAgent());
        return result;
    }
}
