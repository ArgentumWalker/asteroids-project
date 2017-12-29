package ru.spbau.svidchenko.asteroids_project.agentmodel;

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
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentsBuilder {

    public static List<GunnerAgent> getDefaultGunnerAgents() {
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new DoNothingGunnerAgent());
        //result.add(new RandomShootGunnerAgent());
        //result.add(new LeftRollNShootGunnerAgent());
        //result.add(new RightRollNShootGunnerAgent());
        result.add(new ShootClosestGunnerAgent());
        //result.add(new ShootClosestToDirectionGunnerAgent(0.25));
        //result.add(new ShootClosestToDirectionGunnerAgent(0.75));
        //result.add(new ShootClosestToDirectionGunnerAgent(1.25));
        //result.add(new ShootClosestToDirectionGunnerAgent(1.75));
        //result.add(new ShootClosestToDirectionGunnerAgent(2.25));
        //result.add(new ShootClosestToDirectionGunnerAgent(2.75));
        //result.add(new ShootForwardGunnerAgent());
        return result;
    }

    public static List<PilotAgent> getDefaultPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        result.add(new DoNothingPilotAgent());
        result.add(new FlyForwardPilotAgent());
        //result.add(new RandomFlyPilotAgent());
        //result.add(new LeftRollNFlyPilotAgent());
        //result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    public static List<PilotAgent> getImprovedPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        List<Pair<String, Point>> powers = Arrays.asList(
                Pair.of("Left", Point.with(2, -2)),
                Pair.of("Right", Point.with(2, 2)),
                Pair.of("Forward", Point.with(5, 0)),
                Pair.of("ForwardForced", Point.with(8, 0)),
                Pair.of("Backward", Point.with(-5, 0)),
                Pair.of("Hold", Point.with(0, 0))
        );
        List<Pair<String, Double>> doudgeCoef = Arrays.asList(
                Pair.of("Normal", 1e5),
                Pair.of("Active", 2e5),
                Pair.of("Inactive", 6e4),
                Pair.of("Stay", 3e4)
        );
        List<Pair<String, Double>> angleReaction = Arrays.asList(
                Pair.of("SmallAngle", 1./4),
                Pair.of("NormalAngle", 1./2),
                Pair.of("BigAngle", 1./Math.sqrt(1.8))
        );
        //72 pilots
        for (Pair<String, Point> power : powers) {
            for (Pair<String, Double> coef : doudgeCoef) {
                for (Pair<String, Double> angle : angleReaction) {
                    result.add(new AvoidPilotAgent(
                            PowerFunctions.getSquaredPowerFunction(coef.second()),
                            power.second(),
                            ActionFunctions.getSinActionFunction(1, angle.second()),
                            power.first() + "_" + coef.first() + "_" + angle.first()
                    ));
                }
            }
        }
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


    public static List<PilotAgent> getQLearningPilotsAlt(long gamesCount) {
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


    public static List<PilotAgent> getQLearningPilotsAlt2(long gamesCount) {
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

    public static List<GunnerAgent> getQNetSelectiveGunners() {
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new QNetSelectiveGunnerAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                getDefaultGunnerAgents(),
                10,
                new ExplorationProbability1(6000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95, 0).setRefreshDelay(72 * Constants.TURNS_IN_GAME));
        result.add(new QNetSelectiveGunnerAgent(
                PowerFunctions.getSquaredPowerFunction(1e5),
                getDefaultGunnerAgents(),
                10,
                new ExplorationProbability1(6000 * Constants.TURNS_IN_GAME, 0.1),
                new LearningStep1(0.01),
                0.95, 1).setRefreshDelay(72 * Constants.TURNS_IN_GAME));
        return result;
    }

    public static List<GunnerAgent> getSortedQLearningGunners(long gamesCount, List<SortedEntitiesDataDescriptor> descriptors) {
        List<GunnerAgent> result = new ArrayList<>();
        for (SortedEntitiesDataDescriptor dataDescriptor : descriptors) {
            result.add(new DoubleQLearningSortedGunnerAgent(dataDescriptor,
                    new ExplorationProbability3(288 * Constants.TURNS_IN_GAME, 0.95),
                    new LearningStep1(0.005),
                    0.95,
                    72 * Constants.TURNS_IN_GAME));
        }
        return result;
    }

    public static List<SortedEntitiesDataDescriptor> getSortedDescriptors1() {
        List<SortedEntitiesDataDescriptor> result = new ArrayList<>();
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        /*SortedEntitiesDataDescriptor descriptor1 = new SortedEntitiesDataDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.setLimit(1);
        for (int i = 1; i < max; i++) {
            descriptor1.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor1.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }*/
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
        /*SortedEntitiesDataDescriptor descriptor4 = new SortedEntitiesDataDescriptor();
        descriptor4.setMaxAngle(max);
        descriptor4.setVehicleRelated(true);
        descriptor4.setLimit(1);
        descriptor4.setCloserReward(12.);
        for (int i = 1; i < max; i++) {
            descriptor4.splitAngle(i);
        }
        for (int i = 1; i < 41; i++) {
            descriptor4.splitDistance(i * Constants.STONE_MAX_VELOCITY + Constants.STONE_RADIUS + Constants.SHIP_RADIUS);
        }*/
        //result.add(descriptor1);
        result.add(descriptor2);
        result.add(descriptor3);
        //result.add(descriptor4);
        return result;
    }

    public static List<SortedEntitiesDataDescriptor> getSortedDescriptors2() {
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
