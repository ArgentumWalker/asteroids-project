package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.ExplorationProbability1;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.LearningStep2;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.DoNothingGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.ShootClosestGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;

import java.util.ArrayList;
import java.util.List;

public class AgentsBuilder {

    public List<GunnerAgent> getDefaultGunnerAgents() {
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new DoNothingGunnerAgent());
        //result.add(new LeftRollNShootGunnerAgent());
        //result.add(new RandomShootGunnerAgent());
        //result.add(new RightRollNShootGunnerAgent());
        //result.add(new ShootForwardGunnerAgent());
        //result.add(new ShootClosestGunnerAgent());
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
        result.addAll(getQLearningPolarGunners(gamesCount));
        return result;
    }


    public List<PilotAgent> getPilotAgents(long gamesCount) {
        List<PilotAgent> result = new ArrayList<>();
        result.addAll(getQLearningPolarPilots(gamesCount));
        return result;
    }

    private static List<QLearningPolarPilotAgent> getQLearningPolarPilots(long gamesCount) {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor3 = new PolarGridDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.splitAngle(6);
        descriptor3.splitAngle(max - 6);
        descriptor3.splitAngle(15);
        descriptor3.splitAngle(max - 15);
        descriptor3.splitAngle(25);
        descriptor3.splitAngle(max - 25);
        descriptor3.splitAngle(34);
        descriptor3.splitAngle(max - 34);
        descriptor3.splitAngle(40);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 3);
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
        descriptor4.splitDistance(Constants.STONE_RADIUS * 5);
        PolarGridDescriptor descriptor5 = new PolarGridDescriptor();
        descriptor5.setMaxAngle(max);
        descriptor5.splitAngle(5);
        descriptor5.splitAngle(max - 5);
        descriptor5.splitAngle(10);
        descriptor5.splitAngle(max - 10);
        descriptor5.splitAngle(15);
        descriptor5.splitAngle(max - 15);
        descriptor5.splitAngle(20);
        descriptor5.splitAngle(max - 20);
        descriptor5.splitAngle(25);
        descriptor5.splitAngle(max - 25);
        descriptor5.splitAngle(30);
        descriptor5.splitAngle(max - 30);
        descriptor5.splitAngle(35);
        descriptor5.splitAngle(max - 35);
        descriptor5.splitAngle(40);
        descriptor5.splitDistance(Constants.STONE_RADIUS * 3);
        PolarGridDescriptor descriptor6 = new PolarGridDescriptor();
        descriptor6.setMaxAngle(max);
        descriptor6.splitAngle(5);
        descriptor6.splitAngle(max - 5);
        descriptor6.splitAngle(10);
        descriptor6.splitAngle(max - 10);
        descriptor6.splitAngle(15);
        descriptor6.splitAngle(max - 15);
        descriptor6.splitAngle(20);
        descriptor6.splitAngle(max - 20);
        descriptor6.splitAngle(25);
        descriptor6.splitAngle(max - 25);
        descriptor6.splitAngle(30);
        descriptor6.splitAngle(max - 30);
        descriptor6.splitAngle(35);
        descriptor6.splitAngle(max - 35);
        descriptor6.splitAngle(40);
        descriptor6.splitDistance(Constants.STONE_RADIUS * 5);
        List<QLearningPolarPilotAgent> result = new ArrayList<>();
        result.add(new QLearningPolarPilotAgent(descriptor3,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor5,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor6,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.01),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.075, 1.0), 0.995));
        return result;
    }

    public List<QLearningPolarGunnerAgent> getQLearningPolarGunners(long gamesCount) {
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor1 = new PolarGridDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.splitAngle(5);
        descriptor1.splitAngle(max - 5);
        descriptor1.splitAngle(10);
        descriptor1.splitAngle(max - 10);
        descriptor1.splitAngle(18);
        descriptor1.splitAngle(max - 18);
        descriptor1.splitAngle(30);
        descriptor1.splitAngle(max - 30);
        descriptor1.splitAngle(40);
        descriptor1.splitDistance(Constants.STONE_RADIUS * 4);
        PolarGridDescriptor descriptor2 = new PolarGridDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.splitAngle(5);
        descriptor2.splitAngle(max - 5);
        descriptor2.splitAngle(10);
        descriptor2.splitAngle(max - 10);
        descriptor2.splitAngle(20);
        descriptor2.splitAngle(max - 20);
        descriptor2.splitAngle(40);
        descriptor2.splitDistance(Constants.STONE_RADIUS * 4);
        descriptor2.splitDistance(Constants.STONE_RADIUS * 8);
        PolarGridDescriptor descriptor3 = new PolarGridDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.splitAngle(3);
        descriptor3.splitAngle(max - 3);
        descriptor3.splitAngle(9);
        descriptor3.splitAngle(max - 9);
        descriptor3.splitAngle(20);
        descriptor3.splitAngle(max - 20);
        descriptor3.splitAngle(40);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 5);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 12);
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        descriptor4.splitAngle(4);
        descriptor4.splitAngle(max - 4);
        descriptor4.splitAngle(9);
        descriptor4.splitAngle(max - 9);
        descriptor4.splitAngle(14);
        descriptor4.splitAngle(max - 14);
        descriptor4.splitAngle(19);
        descriptor4.splitAngle(max - 19);
        descriptor4.splitAngle(24);
        descriptor4.splitAngle(max - 24);
        descriptor4.splitAngle(29);
        descriptor4.splitAngle(max - 29);
        descriptor4.splitAngle(34);
        descriptor4.splitAngle(max - 34);
        descriptor4.splitAngle(40);
        descriptor4.splitDistance(Constants.STONE_RADIUS * 9);
        List<QLearningPolarGunnerAgent> result = new ArrayList<>();
        result.add(new QLearningPolarGunnerAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        result.add(new QLearningPolarGunnerAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        result.add(new QLearningPolarGunnerAgent(descriptor3,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        result.add(new QLearningPolarGunnerAgent(descriptor4,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.0075, 1.0), 0.995));
        return result;
    }
}
