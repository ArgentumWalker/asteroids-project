package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.ExplorationProbability1;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.LearningStep2;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.DoNothingGunnerAgent;
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
        result.add(new FlyForwardPilotAgent());
        result.add(new RandomFlyPilotAgent());
        result.add(new LeftRollNFlyPilotAgent());
        result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    public List<GunnerAgent> getGunnerAgents(long gamesCount) {
        List<GunnerAgent> result = new ArrayList<>();
        result.addAll(getQLearningPolarGunners());
        return result;
    }


    public List<PilotAgent> getPilotAgents(long gamesCount) {
        List<PilotAgent> result = new ArrayList<>();
        result.addAll(getQLearningPolarPilots(gamesCount));
        return result;
    }

    private static List<QLearningPolarPilotAgent> getQLearningPolarPilots(long gamesCount) {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
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
        descriptor1.splitDistance(Constants.STONE_RADIUS * 3);
        PolarGridDescriptor descriptor2 = new PolarGridDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.splitAngle(5);
        descriptor2.splitAngle(max - 5);
        descriptor2.splitAngle(10);
        descriptor2.splitAngle(max - 10);
        descriptor2.splitAngle(18);
        descriptor2.splitAngle(max - 18);
        descriptor2.splitAngle(30);
        descriptor2.splitAngle(max - 30);
        descriptor2.splitAngle(40);
        descriptor2.splitDistance(Constants.STONE_RADIUS * 3);
        descriptor2.splitDistance(Constants.STONE_RADIUS * 6);
        //Symmetric
        /*PolarGridDescriptor descriptor3 = new PolarGridDescriptor();
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
        descriptor4.splitDistance(Constants.STONE_RADIUS * 3);
        descriptor4.splitDistance(Constants.STONE_RADIUS * 6);
        PolarGridDescriptor descriptor5 = new PolarGridDescriptor();
        descriptor5.setMaxAngle(max);
        descriptor5.splitAngle(6);
        descriptor5.splitAngle(max - 6);
        descriptor5.splitAngle(15);
        descriptor5.splitAngle(max - 15);
        descriptor5.splitAngle(25);
        descriptor5.splitAngle(max - 25);
        descriptor5.splitAngle(34);
        descriptor5.splitAngle(max - 34);
        descriptor5.splitDistance(Constants.STONE_RADIUS * 3);
        PolarGridDescriptor descriptor6 = new PolarGridDescriptor();
        descriptor6.setMaxAngle(max);
        descriptor6.splitAngle(6);
        descriptor6.splitAngle(max - 6);
        descriptor6.splitAngle(15);
        descriptor6.splitAngle(max - 15);
        descriptor6.splitAngle(25);
        descriptor6.splitAngle(max - 25);
        descriptor6.splitAngle(34);
        descriptor6.splitAngle(max - 34);
        descriptor6.splitDistance(Constants.STONE_RADIUS * 3);
        descriptor6.splitDistance(Constants.STONE_RADIUS * 6);*/
        List<QLearningPolarPilotAgent> result = new ArrayList<>();
        result.add(new QLearningPolarPilotAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                () -> 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                () -> 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                () -> 0.1, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                () -> 0.1, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor1,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.01, 1.0), 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor2,
                new ExplorationProbability1(gamesCount * Constants.TURNS_IN_GAME, 0.05),
                new LearningStep2(Constants.TURNS_IN_GAME, 0.01, 1.0), 0.995));
        return result;
    }

    public static List<QLearningPolarGunnerAgent> getQLearningPolarGunners() {
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        List<QLearningPolarGunnerAgent> result = new ArrayList<>();
        return result;
    }
}
