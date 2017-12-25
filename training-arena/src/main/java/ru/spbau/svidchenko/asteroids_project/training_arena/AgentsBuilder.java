package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.DoubleQLearningSortedGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.QLearningSortedGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.polar_model.QNetPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.selective_model.QNetSelectiveGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved.ActionFunctions;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved.AvoidPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved.PowerFunctions;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance.SortedEntitiesDataDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

import java.util.ArrayList;
import java.util.List;

public class AgentsBuilder {

    public List<GunnerAgent> getDefaultGunnerAgents() {
        List<GunnerAgent> result = new ArrayList<>();
        //result.add(new DoNothingGunnerAgent());
        //result.add(new RandomShootGunnerAgent());
        result.add(new LeftRollNShootGunnerAgent());
        result.add(new RightRollNShootGunnerAgent());
        result.add(new ShootClosestGunnerAgent());
        result.add(new ShootClosestToDirectionGunnerAgent(0.25));
        result.add(new ShootClosestToDirectionGunnerAgent(0.75));
        result.add(new ShootClosestToDirectionGunnerAgent(1.25));
        result.add(new ShootClosestToDirectionGunnerAgent(1.75));
        result.add(new ShootClosestToDirectionGunnerAgent(2.25));
        result.add(new ShootClosestToDirectionGunnerAgent(2.75));
        result.add(new ShootForwardGunnerAgent());
        return result;
    }

    public List<PilotAgent> getDefaultPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        result.add(new DoNothingPilotAgent());
        result.add(new FlyForwardPilotAgent());
        //result.add(new RandomFlyPilotAgent());
        //result.add(new LeftRollNFlyPilotAgent());
        //result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    public List<PilotAgent> getImprovedPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        result.add(new AvoidPilotAgent(
                PowerFunctions.getSquaredPowerFunction(2e4),
                Point.with(0, 0),
                ActionFunctions.getSinActionFunction(1, 0.5), "Stay"));
        result.add(new AvoidPilotAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                Point.with(0, 0),
                ActionFunctions.getSinActionFunction(1, 0.5), "Normal"));
        result.add(new AvoidPilotAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                Point.with(6, 0),
                ActionFunctions.getSinActionFunction(1, 0.5), "Forward"));
        result.add(new AvoidPilotAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                Point.with(9, 0),
                ActionFunctions.getSinForwardActionFunction(1, 0.5), "ForwardForced"));
        result.add(new AvoidPilotAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                Point.with(2, -2),
                ActionFunctions.getSinActionFunction(1, 0.5), "Left"));
        result.add(new AvoidPilotAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                Point.with(2, 2),
                ActionFunctions.getSinActionFunction(1, 0.5), "Right"));
        return result;
    }

    public List<PilotAgent> getFlyForwardPilotAgent() {
        List<PilotAgent> result = new ArrayList<>();
        result.add(new FlyForwardPilotAgent());
        return result;
    }

    public List<PilotAgent> getAlternateDefaultPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        //result.add(new DoNothingPilotAgent());
        //result.add(new FlyForwardPilotAgent());
        //result.add(new RandomFlyPilotAgent());
        result.add(new LeftRollNFlyPilotAgent());
        //result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    public List<GunnerAgent> getGunnerAgents(long gamesCount) {
        List<GunnerAgent> result = new ArrayList<>();
        //TODO
        return result;
    }


    public List<PilotAgent> getPilotAgents(long gamesCount) {
        List<PilotAgent> result = new ArrayList<>();
        result.addAll(getQLearningPilots(gamesCount));
        return result;
    }

    private static List<PilotAgent> getQLearningPilots(long gamesCount) {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        for (int i = 1; i < max / 2; i++) {
            descriptor4.splitAngle(i * 2);
        }
        for (int i = 1; i < 31; i++) {
            descriptor4.splitDistance(i * Constants.STONE_RADIUS / 6 + Constants.SHIP_RADIUS + Constants.STONE_RADIUS);
        }
        List<PilotAgent> result = new ArrayList<>();
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.4),
                new LearningStep1(0.002),
                0.97).setBonusReward(0, 6.));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.4),
                new LearningStep1(0.002),
                0.97).setBonusReward(0, 8.));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.4),
                new LearningStep1(0.002),
                0.97).setBonusReward(0, 12.));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.4),
                new LearningStep1(0.002),
                0.97).setBonusReward(0, 16.));
        return result;
    }


    public List<PilotAgent> getQLearningPilotsAlt(long gamesCount) {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        for (int i = 1; i < max / 2; i++) {
            descriptor4.splitAngle(i * 2);
        }
        for (int i = 1; i < 31; i++) {
            descriptor4.splitDistance(i * Constants.STONE_RADIUS / 6 + Constants.SHIP_RADIUS + Constants.STONE_RADIUS);
        }
        List<PilotAgent> result = new ArrayList<>();
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.96));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.97));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.98));
        return result;
    }


    public List<PilotAgent> getQLearningPilotsAlt2(long gamesCount) {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        for (int i = 1; i < max / 2; i++) {
            descriptor4.splitAngle(i * 2);
        }
        for (int i = 1; i < 31; i++) {
            descriptor4.splitDistance(i * Constants.STONE_RADIUS / 6 + Constants.SHIP_RADIUS + Constants.STONE_RADIUS);
        }
        List<PilotAgent> result = new ArrayList<>();
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.2),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.3),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetPolarPilotAgent(descriptor4,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.4),
                new LearningStep1(0.01),
                0.95));
        return result;
    }

    public List<GunnerAgent> getQNetSelectiveGunners() {
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new QNetSelectiveGunnerAgent(
                true,
                false,
                false,
                false,
                PowerFunctions.getSquaredPowerFunction(1e5),
                getDefaultGunnerAgents(),
                10,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetSelectiveGunnerAgent(
                true,
                true,
                false,
                false,
                PowerFunctions.getSquaredPowerFunction(1e5),
                getDefaultGunnerAgents(),
                10,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetSelectiveGunnerAgent(
                true,
                true,
                true,
                false,
                PowerFunctions.getSquaredPowerFunction(1e5),
                getDefaultGunnerAgents(),
                10,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95));
        result.add(new QNetSelectiveGunnerAgent(
                true,
                true,
                true,
                true,
                PowerFunctions.getSquaredPowerFunction(1e5),
                getDefaultGunnerAgents(),
                10,
                new ExplorationProbability1(4000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95));
        return result;
    }

    public List<GunnerAgent> getSortedQLearningGunners(long gamesCount, List<SortedEntitiesDataDescriptor> descriptors) {
        List<GunnerAgent> result = new ArrayList<>();
        for (SortedEntitiesDataDescriptor dataDescriptor : descriptors) {
            result.add(new DoubleQLearningSortedGunnerAgent(dataDescriptor,
                    new ExplorationProbability3(100 * Constants.TURNS_IN_GAME, 0.95),
                    new LearningStep1(0.005),
                    0.95,
                    25 * Constants.TURNS_IN_GAME));
        }
        return result;
    }

    public List<SortedEntitiesDataDescriptor> getSortedDescriptors1() {
        List<SortedEntitiesDataDescriptor> result = new ArrayList<>();
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        SortedEntitiesDataDescriptor descriptor1 = new SortedEntitiesDataDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor1.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor2 = new SortedEntitiesDataDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.setLimit(1);
        descriptor2.setCloserReward(12.);
        for (int i = 1; i < max; i++) {
            descriptor2.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor2.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor3 = new SortedEntitiesDataDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.setVehicleRelated(true);
        descriptor3.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor3.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor3.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor4 = new SortedEntitiesDataDescriptor();
        descriptor4.setMaxAngle(max);
        descriptor4.setVehicleRelated(true);
        descriptor4.setLimit(1);
        descriptor4.setCloserReward(12.);
        for (int i = 1; i < max; i++) {
            descriptor4.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor4.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        result.add(descriptor1);
        result.add(descriptor2);
        result.add(descriptor3);
        result.add(descriptor4);
        return result;
    }

    public List<SortedEntitiesDataDescriptor> getSortedDescriptors2() {
        List<SortedEntitiesDataDescriptor> result = new ArrayList<>();
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        SortedEntitiesDataDescriptor descriptor1 = new SortedEntitiesDataDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor1.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor2 = new SortedEntitiesDataDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.setLimit(1);
        descriptor2.setCloserReward(12.);
        for (int i = 1; i < max; i++) {
            descriptor2.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor2.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor3 = new SortedEntitiesDataDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.setVehicleRelated(true);
        descriptor3.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor3.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor3.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        SortedEntitiesDataDescriptor descriptor4 = new SortedEntitiesDataDescriptor();
        descriptor4.setMaxAngle(max);
        descriptor4.setVehicleRelated(true);
        descriptor4.setLimit(1);
        descriptor4.setCloserReward(12.);
        for (int i = 1; i < max; i++) {
            descriptor4.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor4.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }
        result.add(descriptor1);
        result.add(descriptor2);
        result.add(descriptor3);
        result.add(descriptor4);
        return result;
    }
}
